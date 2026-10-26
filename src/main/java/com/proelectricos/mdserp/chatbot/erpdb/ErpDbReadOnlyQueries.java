package com.proelectricos.mdserp.chatbot.erpdb;

import com.proelectricos.mdserp.chatbot.config.ChatbotProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
                 , h.orderHeaderClientOrder AS OrdenCompra
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

    // Esquema completo de ErpDb (sin tablas de respaldo ni de sistema), para darlo al modelo y que no lo explore
    private static final String SQL_ESQUEMA_TABLAS = """
            SELECT t.TABLE_NAME + N'(' + STUFF((SELECT N', ' + c.COLUMN_NAME + N' ' + c.DATA_TYPE
                          FROM INFORMATION_SCHEMA.COLUMNS AS c
                          WHERE c.TABLE_SCHEMA = t.TABLE_SCHEMA AND c.TABLE_NAME = t.TABLE_NAME
                          ORDER BY c.ORDINAL_POSITION
                          FOR XML PATH(''), TYPE).value('.', 'nvarchar(max)'), 1, 2, N'') + N')'
            FROM INFORMATION_SCHEMA.TABLES AS t
            WHERE t.TABLE_SCHEMA = 'dbo' AND t.TABLE_TYPE IN ('BASE TABLE', 'VIEW')
              AND t.TABLE_NAME NOT IN ('sysdiagrams', 'TriggerErrorLog') AND t.TABLE_NAME NOT LIKE '%[_]Backup'
            ORDER BY t.TABLE_NAME""";

    private static final String SQL_ESQUEMA_RELACIONES = """
            SELECT OBJECT_NAME(fk.parent_object_id) + N'.' + COL_NAME(fkc.parent_object_id, fkc.parent_column_id)
                 + N' -> ' + OBJECT_NAME(fk.referenced_object_id) + N'.' + COL_NAME(fkc.referenced_object_id, fkc.referenced_column_id)
            FROM sys.foreign_keys AS fk
            INNER JOIN sys.foreign_key_columns AS fkc ON fkc.constraint_object_id = fk.object_id
            ORDER BY 1""";

    private static final String SQL_ESTADOS = """
            SELECT CAST(orderReferStatusId AS nvarchar(10)) + N' = ' + orderReferStatusName
            FROM dbo.OrderReferStatus ORDER BY orderReferStatusId""";

    // Reportes de ventas: líneas no anuladas/canceladas de pedidos en un rango de fechas, con vendedor del pedido
    // (o el de la sede) y filtro opcional de vendedor por código exacto o parte del nombre.
    // Parámetros: fecha inicial, fecha final, vendedor x3 (puede ser NULL)
    private static final String FROM_VENTAS = """
            FROM dbo.OrderHeader AS h
            INNER JOIN dbo.Branch AS b ON b.branchId = h.orderHeaderBranch
            LEFT JOIN dbo.Vendor AS v ON v.vendorId = ISNULL(h.orderHeaderVendor, b.branchVendor)
            LEFT JOIN dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            INNER JOIN dbo.OrderReference AS r ON r.orderReferOrderHeader = h.orderHeaderId
            INNER JOIN dbo.OrderReferStatus AS s ON s.orderReferStatusId = r.orderReferStatus
            WHERE h.orderHeaderDate BETWEEN ? AND ?
              AND s.orderReferStatusName NOT IN (N'Anulado', N'Cancelado')
              AND (? IS NULL OR v.vendorCode = ? OR tv.thirdPartyName LIKE N'%' + ? + N'%')
            """;

    private static final String SQL_VENTAS_POR_VENDEDOR = """
            SELECT ISNULL(tv.thirdPartyName, N'Sin vendedor') AS Vendedor
                 , v.vendorCode AS CodigoVendedor
                 , COUNT(DISTINCT h.orderHeaderId) AS Pedidos
                 , SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS Valor
            """ + FROM_VENTAS + """
            GROUP BY tv.thirdPartyName, v.vendorCode
            ORDER BY Valor DESC""";

    // Semanas de lunes a domingo, recortadas al rango consultado (1900-01-01 fue lunes)
    private static final String SQL_VENTAS_POR_SEMANA = """
            SELECT ISNULL(tv.thirdPartyName, N'Sin vendedor') AS Vendedor
                 , semana.Inicio AS InicioSemana
                 , semana.Fin AS FinSemana
                 , COUNT(DISTINCT h.orderHeaderId) AS Pedidos
                 , SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS Valor
            """ + FROM_VENTAS.replace("WHERE", """
            CROSS APPLY (SELECT DATEADD(DAY, -(DATEDIFF(DAY, '19000101', h.orderHeaderDate) % 7), h.orderHeaderDate) AS Lunes) AS l
            CROSS APPLY (SELECT CASE WHEN l.Lunes < ? THEN ? ELSE l.Lunes END AS Inicio
                              , CASE WHEN DATEADD(DAY, 6, l.Lunes) > ? THEN ? ELSE DATEADD(DAY, 6, l.Lunes) END AS Fin) AS semana
            WHERE""") + """
            GROUP BY tv.thirdPartyName, semana.Inicio, semana.Fin
            ORDER BY Vendedor, InicioSemana""";

    // Línea de negocio = primer nivel de ReferClassification bajo "productos"; lo demás va a "Otros"
    private static final String CTE_LINEA_NEGOCIO = """
            WITH arbol AS (
                SELECT referClassId, referClassFather,
                       CAST(referClassName AS nvarchar(100)) AS linea, CAST(referClassName AS nvarchar(100)) AS raiz
                FROM dbo.ReferClassification WHERE referClassFather IS NULL
                UNION ALL
                SELECT c.referClassId, c.referClassFather,
                       CASE WHEN a.referClassFather IS NULL THEN CAST(c.referClassName AS nvarchar(100)) ELSE a.linea END,
                       a.raiz
                FROM dbo.ReferClassification AS c INNER JOIN arbol AS a ON c.referClassFather = a.referClassId)
            """;

    private static final String LINEA_NEGOCIO = "CASE WHEN a.raiz = N'productos' THEN a.linea ELSE N'Otros' END";

    private static final String JOIN_LINEA_NEGOCIO = """
            INNER JOIN dbo.Reference AS ref ON ref.referId = r.orderReferReference
            LEFT JOIN arbol AS a ON a.referClassId = ref.referClassification
            WHERE""";

    private static final String SQL_VENTAS_POR_LINEA = CTE_LINEA_NEGOCIO + "SELECT " + LINEA_NEGOCIO + """
             AS LineaNegocio
                 , COUNT(DISTINCT h.orderHeaderId) AS Pedidos
                 , SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS Valor
            """ + FROM_VENTAS.replace("WHERE", JOIN_LINEA_NEGOCIO) + "GROUP BY " + LINEA_NEGOCIO + """

            ORDER BY Valor DESC""";

    private static final String SQL_VENTAS_POR_LINEA_Y_VENDEDOR = CTE_LINEA_NEGOCIO + "SELECT " + LINEA_NEGOCIO + """
             AS LineaNegocio
                 , ISNULL(tv.thirdPartyName, N'Sin vendedor') AS Vendedor
                 , COUNT(DISTINCT h.orderHeaderId) AS Pedidos
                 , SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS Valor
            """ + FROM_VENTAS.replace("WHERE", JOIN_LINEA_NEGOCIO) + "GROUP BY " + LINEA_NEGOCIO + """
            , tv.thirdPartyName
            ORDER BY LineaNegocio, Valor DESC""";

    // Informe semanal de ventas por vendedor (basado en el script de solo lectura del mismo nombre): cuatro consultas
    // sobre los pedidos de un rango de fechas. Parámetros de cada una: fecha inicial, fecha final (ambas incluidas).
    // Estados de línea: 1 Cancelado, 2 Aprobado, 3 Detenido, 4 En Revisión, 5 Anulado; el Total excluye 1 y 5.
    // Algunos nombres de vendedor traen CR/LF al final; se limpian.
    private static final String NOMBRE_VENDEDOR =
            "RTRIM(REPLACE(REPLACE(tv.thirdPartyName, CHAR(13), N''), CHAR(10), N''))";

    // Todos los vendedores; los que no vendieron en el periodo salen en cero
    private static final String SQL_INFORME_RESUMEN = """
            WITH Pedidos AS (
                SELECT h.orderHeaderId
                     , ISNULL(h.orderHeaderVendor, b.branchVendor) AS vendorId
                     , b.branchClient AS clientId
                FROM dbo.OrderHeader AS h
                INNER JOIN dbo.Branch AS b ON b.branchId = h.orderHeaderBranch
                WHERE h.orderHeaderDate >= ? AND h.orderHeaderDate < DATEADD(DAY, 1, ?)
            ),
            Totales AS (
                SELECT p.vendorId
                     , COUNT(DISTINCT p.orderHeaderId) AS Pedidos
                     , COUNT(DISTINCT p.clientId) AS Clientes
                     , COUNT(r.orderReferId) AS Lineas
                     , ISNULL(SUM(CASE WHEN r.orderReferStatus NOT IN (1, 5)
                                       THEN r.orderReferQuantity END), 0) AS Unidades
                     , ISNULL(SUM(CASE WHEN r.orderReferStatus NOT IN (1, 5)
                                       THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS Total
                     , ISNULL(SUM(CASE WHEN r.orderReferStatus = 2
                                       THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS TotalAprobado
                     , ISNULL(SUM(CASE WHEN r.orderReferStatus = 4
                                       THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS TotalEnRevision
                     , ISNULL(SUM(CASE WHEN r.orderReferStatus = 3
                                       THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS TotalDetenido
                     , ISNULL(SUM(CASE WHEN r.orderReferStatus IN (1, 5)
                                       THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS TotalCanceladoAnulado
                FROM Pedidos AS p
                LEFT JOIN dbo.OrderReference AS r ON r.orderReferOrderHeader = p.orderHeaderId
                GROUP BY p.vendorId
            )
            SELECT ISNULL(v.vendorCode, N'(sin)') AS CodigoVendedor
                 , ISNULL(""" + NOMBRE_VENDEDOR + """
            , N'(sin vendedor)') AS Vendedor
                 , ISNULL(t.Pedidos, 0) AS Pedidos
                 , ISNULL(t.Clientes, 0) AS Clientes
                 , ISNULL(t.Lineas, 0) AS Lineas
                 , ISNULL(t.Unidades, 0) AS Unidades
                 , ISNULL(t.Total, 0) AS Total
                 , CAST(100.0 * ISNULL(t.Total, 0)
                        / NULLIF(SUM(ISNULL(t.Total, 0)) OVER (), 0) AS decimal(5, 1)) AS Participacion
                 , CASE WHEN ISNULL(t.Pedidos, 0) = 0 THEN 0 ELSE t.Total / t.Pedidos END AS PromedioPedido
                 , ISNULL(t.TotalAprobado, 0) AS TotalAprobado
                 , ISNULL(t.TotalEnRevision, 0) AS TotalEnRevision
                 , ISNULL(t.TotalDetenido, 0) AS TotalDetenido
                 , ISNULL(t.TotalCanceladoAnulado, 0) AS TotalCanceladoAnulado
            FROM dbo.Vendor AS v
            INNER JOIN dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            FULL OUTER JOIN Totales AS t ON t.vendorId = v.vendorId
            ORDER BY ISNULL(t.Total, 0) DESC, v.vendorCode""";

    private static final String SQL_INFORME_POR_DIA = """
            SELECT h.orderHeaderDate AS Fecha
                 , v.vendorCode AS CodigoVendedor
                 , """ + NOMBRE_VENDEDOR + """
             AS Vendedor
                 , COUNT(DISTINCT h.orderHeaderId) AS Pedidos
                 , ISNULL(SUM(CASE WHEN r.orderReferStatus NOT IN (1, 5)
                                   THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS Total
            FROM dbo.OrderHeader AS h
            INNER JOIN dbo.Branch AS b ON b.branchId = h.orderHeaderBranch
            LEFT JOIN dbo.Vendor AS v ON v.vendorId = ISNULL(h.orderHeaderVendor, b.branchVendor)
            LEFT JOIN dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            LEFT JOIN dbo.OrderReference AS r ON r.orderReferOrderHeader = h.orderHeaderId
            WHERE h.orderHeaderDate >= ? AND h.orderHeaderDate < DATEADD(DAY, 1, ?)
            GROUP BY h.orderHeaderDate, v.vendorCode, tv.thirdPartyName
            ORDER BY h.orderHeaderDate, v.vendorCode""";

    private static final String SQL_INFORME_PEDIDOS = """
            SELECT v.vendorCode AS CodigoVendedor
                 , """ + NOMBRE_VENDEDOR + """
             AS Vendedor
                 , h.orderHeaderDate AS Fecha
                 , h.orderHeaderNumber AS Pedido
                 , tc.thirdPartyName AS Cliente
                 , b.branchCity AS Ciudad
                 , COUNT(r.orderReferId) AS Lineas
                 , ISNULL(SUM(CASE WHEN r.orderReferStatus NOT IN (1, 5)
                                   THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS Total
                 , ISNULL(SUM(CASE WHEN r.orderReferStatus = 4
                                   THEN r.orderReferQuantity * r.orderReferUnitPrice END), 0) AS TotalEnRevision
            FROM dbo.OrderHeader AS h
            INNER JOIN dbo.Branch AS b ON b.branchId = h.orderHeaderBranch
            INNER JOIN dbo.Client AS c ON c.clientId = b.branchClient
            INNER JOIN dbo.ThirdParty AS tc ON tc.thirdPartyId = c.clientThirdParty
            LEFT JOIN dbo.Vendor AS v ON v.vendorId = ISNULL(h.orderHeaderVendor, b.branchVendor)
            LEFT JOIN dbo.ThirdParty AS tv ON tv.thirdPartyId = v.vendorThirdParty
            LEFT JOIN dbo.OrderReference AS r ON r.orderReferOrderHeader = h.orderHeaderId
            WHERE h.orderHeaderDate >= ? AND h.orderHeaderDate < DATEADD(DAY, 1, ?)
            GROUP BY v.vendorCode, tv.thirdPartyName, h.orderHeaderDate, h.orderHeaderNumber,
                     tc.thirdPartyName, b.branchCity
            ORDER BY v.vendorCode, h.orderHeaderDate, h.orderHeaderNumber""";

    // Referencias repetidas dentro de un mismo pedido: posible doble digitación que infla el total
    private static final String SQL_INFORME_REPETIDAS = """
            SELECT v.vendorCode AS CodigoVendedor
                 , h.orderHeaderNumber AS Pedido
                 , ref.referCod AS CodigoReferencia
                 , ref.referName AS Nombre
                 , COUNT(*) AS Veces
                 , SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS TotalReferencia
                 , SUM(r.orderReferQuantity * r.orderReferUnitPrice)
                     - MIN(r.orderReferQuantity * r.orderReferUnitPrice) AS ValorPosibleDuplicado
            FROM dbo.OrderHeader AS h
            INNER JOIN dbo.Branch AS b ON b.branchId = h.orderHeaderBranch
            LEFT JOIN dbo.Vendor AS v ON v.vendorId = ISNULL(h.orderHeaderVendor, b.branchVendor)
            INNER JOIN dbo.OrderReference AS r ON r.orderReferOrderHeader = h.orderHeaderId
            INNER JOIN dbo.Reference AS ref ON ref.referId = r.orderReferReference
            WHERE h.orderHeaderDate >= ? AND h.orderHeaderDate < DATEADD(DAY, 1, ?)
              AND r.orderReferStatus NOT IN (1, 5)
            GROUP BY v.vendorCode, h.orderHeaderNumber, ref.referCod, ref.referName
            HAVING COUNT(*) > 1
            ORDER BY h.orderHeaderNumber, ref.referCod""";

    // Columnas del resumen por vendedor que se suman para el total del periodo
    private static final List<String> TOTALES_INFORME = List.of("Pedidos", "Lineas", "Unidades", "Total",
            "TotalAprobado", "TotalEnRevision", "TotalDetenido", "TotalCanceladoAnulado");

    // Los reportes agregados tienen pocas filas (vendedores x semanas o líneas); se permiten más que en consultar_sql
    private static final int MAX_FILAS_REPORTE = 1000;

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
                    return leer(rs, maxRows);
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

    /** Pedidos y valor vendido por vendedor en un rango de fechas (ambas incluidas). */
    public Map<String, Object> ventasPorVendedor(LocalDate desde, LocalDate hasta, String vendedor) throws SQLException {
        return reporte(SQL_VENTAS_POR_VENDEDOR, desde, hasta, filtroVendedor(desde, hasta, vendedor));
    }

    /** Pedidos y valor vendido por semana (lunes a domingo) y vendedor en un rango, normalmente un mes. */
    public Map<String, Object> ventasPorSemana(LocalDate desde, LocalDate hasta, String vendedor) throws SQLException {
        List<Object> parametros = new ArrayList<>(List.of(Date.valueOf(desde), Date.valueOf(desde),
                Date.valueOf(hasta), Date.valueOf(hasta)));
        parametros.addAll(filtroVendedor(desde, hasta, vendedor));
        Map<String, Object> resultado = reporte(SQL_VENTAS_POR_SEMANA, desde, hasta, parametros);
        // Total del periodo por vendedor, para no depender de la aritmética del modelo
        Map<String, BigDecimal> totalPorVendedor = new LinkedHashMap<>();
        for (Map<String, Object> fila : filas(resultado)) {
            totalPorVendedor.merge(String.valueOf(fila.get("Vendedor")), decimal(fila.get("Valor")), BigDecimal::add);
        }
        resultado.put("total_por_vendedor", totalPorVendedor);
        return resultado;
    }

    /**
     * Pedidos y valor vendido por línea de negocio (conduit, celdas y tableros...), opcionalmente también por vendedor.
     * Un pedido con referencias de varias líneas cuenta en cada una.
     */
    public Map<String, Object> ventasPorLinea(LocalDate desde, LocalDate hasta, String vendedor, boolean porVendedor)
            throws SQLException {
        return reporte(porVendedor ? SQL_VENTAS_POR_LINEA_Y_VENDEDOR : SQL_VENTAS_POR_LINEA,
                desde, hasta, filtroVendedor(desde, hasta, vendedor));
    }

    /**
     * Informe semanal de ventas de un rango de fechas (ambas incluidas): resumen por vendedor (todos, aunque no
     * hayan vendido), ventas por día y vendedor, detalle de pedidos y referencias repetidas en un mismo pedido.
     */
    public Map<String, Object> informeSemanal(LocalDate desde, LocalDate hasta) throws SQLException {
        validarRango(desde, hasta);
        List<Object> periodo = List.of(Date.valueOf(desde), Date.valueOf(hasta));
        return enTransaccionDeLectura(conn -> {
            List<Map<String, Object>> resumen = filas(consultar(conn, SQL_INFORME_RESUMEN, periodo));
            Map<String, Object> pedidos = consultar(conn, SQL_INFORME_PEDIDOS, periodo);
            // Totales del periodo calculados aquí para no depender de la aritmética del modelo
            Map<String, BigDecimal> totales = new LinkedHashMap<>();
            for (String columna : TOTALES_INFORME) {
                totales.put(columna, resumen.stream()
                        .map(fila -> decimal(fila.get(columna)))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
            }

            Map<String, Object> resultado = new LinkedHashMap<>();
            resultado.put("desde", desde.toString());
            resultado.put("hasta", hasta.toString());
            resultado.put("resumen_por_vendedor", resumen);
            resultado.put("totales_periodo", totales);
            resultado.put("ventas_por_dia", filas(consultar(conn, SQL_INFORME_POR_DIA, periodo)));
            resultado.put("pedidos", filas(pedidos));
            resultado.put("pedidos_truncados", pedidos.get("truncado"));
            resultado.put("referencias_repetidas", filas(consultar(conn, SQL_INFORME_REPETIDAS, periodo)));
            resultado.put("nota", "Valores sin impuestos. Total excluye líneas canceladas y anuladas, "
                    + "que van aparte en TotalCanceladoAnulado.");
            return resultado;
        });
    }

    private static List<Object> filtroVendedor(LocalDate desde, LocalDate hasta, String vendedor) {
        validarRango(desde, hasta);
        String filtro = vendedor == null || vendedor.isBlank() ? null : vendedor.trim();
        return Arrays.asList(Date.valueOf(desde), Date.valueOf(hasta), filtro, filtro, filtro);
    }

    private static void validarRango(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha final " + hasta + " es anterior a la inicial " + desde);
        }
    }

    private Map<String, Object> consultar(Connection conn, String sql, List<Object> parametros) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setMaxRows(MAX_FILAS_REPORTE + 1);
            ps.setQueryTimeout(queryTimeoutSeconds);
            for (int i = 0; i < parametros.size(); i++) {
                // El único parámetro que puede ser null es el filtro de vendedor (texto)
                if (parametros.get(i) == null) {
                    ps.setNull(i + 1, Types.NVARCHAR);
                } else {
                    ps.setObject(i + 1, parametros.get(i));
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                return leer(rs, MAX_FILAS_REPORTE);
            }
        }
    }

    private Map<String, Object> reporte(String sql, LocalDate desde, LocalDate hasta, List<Object> parametros)
            throws SQLException {
        Map<String, Object> resultado = enTransaccionDeLectura(conn -> consultar(conn, sql, parametros));
        resultado.put("desde", desde.toString());
        resultado.put("hasta", hasta.toString());
        resultado.put("valor_total_sin_impuestos", filas(resultado).stream()
                .map(fila -> decimal(fila.get("Valor")))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        resultado.put("nota", "Valores sin impuestos; excluye líneas anuladas y canceladas.");
        return resultado;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> filas(Map<String, Object> resultado) {
        return (List<Map<String, Object>>) resultado.get("filas");
    }

    private static BigDecimal decimal(Object valor) {
        return valor instanceof Number ? new BigDecimal(valor.toString()) : BigDecimal.ZERO;
    }

    /** Tablas con columnas y tipos, llaves foraneas y estados de linea de ErpDb, en texto compacto para el prompt. */
    public String describirEsquema() throws SQLException {
        return enTransaccionDeLectura(conn -> "Tablas (dbo):\n" + lineas(conn, SQL_ESQUEMA_TABLAS)
                + "\nLlaves foraneas:\n" + lineas(conn, SQL_ESQUEMA_RELACIONES)
                + "\nOrderReferStatus (id = nombre):\n" + lineas(conn, SQL_ESTADOS));
    }

    private String lineas(Connection conn, String sql) throws SQLException {
        StringBuilder texto = new StringBuilder();
        try (Statement st = conn.createStatement()) {
            st.setQueryTimeout(queryTimeoutSeconds);
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    texto.append("- ").append(rs.getString(1)).append('\n');
                }
            }
        }
        return texto.toString();
    }

    private Map<String, Object> consultarConNumero(Connection conn, String sql, int numero) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setMaxRows(maxRows + 1);
            ps.setQueryTimeout(queryTimeoutSeconds);
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return leer(rs, maxRows);
            }
        }
    }

    private Map<String, Object> leer(ResultSet rs, int limite) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        List<String> columnas = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            columnas.add(meta.getColumnLabel(i));
        }
        List<Map<String, Object>> filas = new ArrayList<>();
        boolean truncado = false;
        while (rs.next()) {
            if (filas.size() == limite) {
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
