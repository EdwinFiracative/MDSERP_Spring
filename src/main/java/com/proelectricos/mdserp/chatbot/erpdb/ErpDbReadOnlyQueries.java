package com.proelectricos.mdserp.chatbot.erpdb;

import com.proelectricos.mdserp.chatbot.config.ChatbotProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consultas del chatbot contra ErpDb con un pool de conexiones propio (no el de JPA).
 * Toda consulta corre en una transacción que siempre se deshace (rollback), con límite de filas y de tiempo.
 */
@Component
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class ErpDbReadOnlyQueries {

    // Basadas en erpdb_Pedido_Detalle.sql; se filtra por número de pedido directamente
    private static final String SQL_PEDIDO_ENCABEZADO = """
            SELECT h.orderHeaderNumber AS Pedido
                 , h.orderHeaderDate AS Fecha
                 , h.orderHeaderClientOrder AS OrdenCliente
                 , RTRIM(b.branchCode) AS Sede
                 , tc.thirdPartyName AS Cliente
                 , b.branchAddress AS Direccion
                 , b.branchCity AS Ciudad
                 , CAST(tc.thirdPartyIdentNumber AS nvarchar(20))
                     + ISNULL(N'-' + CAST(tc.thirdPartyVerifDigit AS nvarchar(1)), N'') AS NIT
                 , c.clientCrediCondition AS CondicionCliente
                 , h.orderHeaderPaymeConditions AS CondicionPagoPedido
                 , v.vendorCode AS CodigoVendedor
                 , tv.thirdPartyName AS Vendedor
                 , h.orderHeaderDescription AS Descripcion
                 , STUFF((SELECT N' ' + n.orderNoteText
                          FROM dbo.OrderNote AS n
                          WHERE n.orderNoteOrderHeader = h.orderHeaderId
                          ORDER BY n.orderNotePosition
                          FOR XML PATH(''), TYPE).value('.', 'nvarchar(max)'), 1, 1, N'') AS Notas
            FROM dbo.OrderHeader AS h
            INNER JOIN dbo.Branch AS b ON b.branchId = h.orderHeaderBranch
            INNER JOIN dbo.Client AS c ON c.clientId = b.branchClient
            INNER JOIN dbo.ThirdParty AS tc ON tc.thirdPartyId = c.clientThirdParty
            LEFT JOIN dbo.Vendor AS v ON v.vendorId = ISNULL(h.orderHeaderVendor, b.branchVendor)
            LEFT JOIN dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            WHERE h.orderHeaderNumber = ?""";

    private static final String SQL_PEDIDO_REFERENCIAS = """
            SELECT r.orderReferPosition AS Item
                 , ref.referCod AS CodigoReferencia
                 , ref.referName AS Nombre
                 , mu.measuUnitCode AS UD
                 , r.orderReferQuantity AS Cantidad
                 , r.orderReferUnitPrice AS ValorUnitario
                 , r.orderReferQuantity * r.orderReferUnitPrice AS ValorTotal
                 , s.orderReferStatusName AS Estado
                 , p.projeName AS Proyecto
                 , r.orderReferDelivDate AS FechaEntrega
            FROM dbo.OrderReference AS r
            INNER JOIN dbo.OrderHeader AS h ON h.orderHeaderId = r.orderReferOrderHeader
            INNER JOIN dbo.Reference AS ref ON ref.referId = r.orderReferReference
            INNER JOIN dbo.MeasurUnit AS mu ON mu.measuUnitId = ref.referMeasuUnit
            INNER JOIN dbo.OrderReferStatus AS s ON s.orderReferStatusId = r.orderReferStatus
            LEFT JOIN dbo.Project AS p ON p.projeId = r.orderReferProject
            WHERE h.orderHeaderNumber = ?
            ORDER BY r.orderReferPosition""";

    private final HikariDataSource dataSource;
    private final int maxRows;
    private final int queryTimeoutSeconds;

    public ErpDbReadOnlyQueries(ChatbotProperties properties) {
        ChatbotProperties.Database db = properties.database();
        this.maxRows = db.maxRows();
        this.queryTimeoutSeconds = db.queryTimeoutSeconds();
        // Sin constructor con HikariConfig: el pool se inicializa en la primera consulta,
        // así la API arranca aunque ErpDb no esté disponible
        this.dataSource = new HikariDataSource();
        dataSource.setPoolName("ErpDbChatbot");
        dataSource.setJdbcUrl(db.url());
        dataSource.setUsername(db.username());
        dataSource.setPassword(db.password());
        dataSource.setMaximumPoolSize(3);
        dataSource.setMinimumIdle(0);
        dataSource.setReadOnly(true);
        dataSource.setAutoCommit(false);
    }

    /** Ejecuta una consulta libre (validada por {@link SqlGuard}) y devuelve columnas y filas. */
    public Map<String, Object> consultarSql(String sql) throws SQLException {
        String consulta = SqlGuard.validar(sql);
        return enTransaccionDeLectura(conn -> {
            try (Statement st = conn.createStatement()) {
                st.setMaxRows(maxRows + 1);
                st.setQueryTimeout(queryTimeoutSeconds);
                try (ResultSet rs = st.executeQuery(consulta)) {
                    return leer(rs);
                }
            }
        });
    }

    /** Encabezado, referencias (con estado) y total de un pedido por su número. */
    public Map<String, Object> consultarPedido(int numeroPedido) throws SQLException {
        return enTransaccionDeLectura(conn -> {
            Map<String, Object> encabezado = consultarConNumero(conn, SQL_PEDIDO_ENCABEZADO, numeroPedido);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> filasEncabezado = (List<Map<String, Object>>) encabezado.get("filas");
            Map<String, Object> resultado = new LinkedHashMap<>();
            if (filasEncabezado.isEmpty()) {
                resultado.put("encontrado", false);
                resultado.put("mensaje", "No existe el pedido " + numeroPedido + " en ErpDb.");
                return resultado;
            }
            Map<String, Object> referencias = consultarConNumero(conn, SQL_PEDIDO_REFERENCIAS, numeroPedido);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> lineas = (List<Map<String, Object>>) referencias.get("filas");
            // El total se calcula aquí para no depender de la aritmética del modelo
            BigDecimal total = lineas.stream()
                    .map(linea -> linea.get("ValorTotal"))
                    .filter(Number.class::isInstance)
                    .map(valor -> new BigDecimal(valor.toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            resultado.put("encontrado", true);
            resultado.put("encabezado", filasEncabezado.get(0));
            resultado.put("referencias", lineas);
            resultado.put("referencias_truncadas", referencias.get("truncado"));
            resultado.put("total_pedido_sin_impuestos", total);
            return resultado;
        });
    }

    private Map<String, Object> consultarConNumero(Connection conn, String sql, int numero) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setMaxRows(maxRows + 1);
            ps.setQueryTimeout(queryTimeoutSeconds);
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return leer(rs);
            }
        }
    }

    private Map<String, Object> leer(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        List<String> columnas = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            columnas.add(meta.getColumnLabel(i));
        }
        List<Map<String, Object>> filas = new ArrayList<>();
        boolean truncado = false;
        while (rs.next()) {
            if (filas.size() == maxRows) {
                truncado = true;
                break;
            }
            Map<String, Object> fila = new LinkedHashMap<>();
            for (int i = 1; i <= columnas.size(); i++) {
                fila.put(columnas.get(i - 1), valor(rs.getObject(i)));
            }
            filas.add(fila);
        }
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("columnas", columnas);
        resultado.put("filas", filas);
        resultado.put("total_filas", filas.size());
        resultado.put("truncado", truncado);
        return resultado;
    }

    // Solo tipos simples para que se serialicen bien en la respuesta de la función
    private static Object valor(Object valor) {
        if (valor == null || valor instanceof Number || valor instanceof Boolean || valor instanceof String) {
            return valor;
        }
        if (valor instanceof byte[]) {
            return "[binario]";
        }
        return valor.toString();
    }

    private <T> T enTransaccionDeLectura(TrabajoJdbc<T> trabajo) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try {
                return trabajo.ejecutar(conn);
            } finally {
                conn.rollback();
            }
        }
    }

    @PreDestroy
    public void cerrar() {
        dataSource.close();
    }

    @FunctionalInterface
    private interface TrabajoJdbc<T> {
        T ejecutar(Connection conn) throws SQLException;
    }
}
