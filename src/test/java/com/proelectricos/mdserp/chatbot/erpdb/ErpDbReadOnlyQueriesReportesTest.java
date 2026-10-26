package com.proelectricos.mdserp.chatbot.erpdb;

import com.proelectricos.mdserp.chatbot.config.ChatbotProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Reportes de ventas del chatbot contra ErpDb (solo lectura): los totales de cada reporte deben coincidir
 * entre sí y con una consulta SQL independiente.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ErpDbReadOnlyQueriesReportesTest {

    private static final YearMonth MES = YearMonth.now().minusMonths(1);
    private static final LocalDate DESDE = MES.atDay(1);
    private static final LocalDate HASTA = MES.atEndOfMonth();

    @Autowired
    private Environment env;

    private ErpDbReadOnlyQueries queries;

    @BeforeAll
    void crear() {
        ChatbotProperties.Database db = new ChatbotProperties.Database(env.getProperty("chatbot.database.url"),
                env.getProperty("chatbot.database.username"), env.getProperty("chatbot.database.password"), 200, 60);
        queries = new ErpDbReadOnlyQueries(new ChatbotProperties(true, null, null, db));
    }

    @AfterAll
    void cerrar() {
        queries.cerrar();
    }

    @Test
    void ventasPorVendedorCoincideConSql() throws Exception {
        Map<String, Object> reporte = queries.ventasPorVendedor(DESDE, HASTA, null);

        Map<String, Object> esperado = queries.consultarSql("""
                SELECT COUNT(DISTINCT h.orderHeaderId) AS Pedidos, SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS Valor
                FROM dbo.OrderHeader h
                JOIN dbo.OrderReference r ON r.orderReferOrderHeader = h.orderHeaderId
                JOIN dbo.OrderReferStatus s ON s.orderReferStatusId = r.orderReferStatus
                WHERE h.orderHeaderDate BETWEEN '%s' AND '%s' AND s.orderReferStatusName NOT IN (N'Anulado', N'Cancelado')
                """.formatted(DESDE, HASTA));
        Map<String, Object> fila = filas(esperado).get(0);

        assertThat(filas(reporte)).as("ventas en " + MES).isNotEmpty();
        assertThat(total(reporte)).isEqualByComparingTo(new BigDecimal(fila.get("Valor").toString()));
        assertThat(filas(reporte).stream().mapToLong(f -> ((Number) f.get("Pedidos")).longValue()).sum())
                .as("cada pedido tiene un solo vendedor")
                .isEqualTo(((Number) fila.get("Pedidos")).longValue());
    }

    @Test
    void ventasPorSemanaSumaLoMismoQuePorVendedor() throws Exception {
        Map<String, Object> porVendedor = queries.ventasPorVendedor(DESDE, HASTA, null);
        Map<String, Object> porSemana = queries.ventasPorSemana(DESDE, HASTA, null);

        assertThat(total(porSemana)).isEqualByComparingTo(total(porVendedor));
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> totalPorVendedor = (Map<String, BigDecimal>) porSemana.get("total_por_vendedor");
        for (Map<String, Object> fila : filas(porVendedor)) {
            assertThat(totalPorVendedor.get(String.valueOf(fila.get("Vendedor"))))
                    .as("total de " + fila.get("Vendedor"))
                    .isEqualByComparingTo(new BigDecimal(fila.get("Valor").toString()));
        }
        assertThat(filas(porSemana)).allSatisfy(fila -> {
            LocalDate inicio = LocalDate.parse(fila.get("InicioSemana").toString());
            LocalDate fin = LocalDate.parse(fila.get("FinSemana").toString());
            assertThat(inicio).isBetween(DESDE, HASTA);
            assertThat(fin).isBetween(inicio, HASTA);
            assertThat(inicio.getDayOfWeek().getValue() == 1 || inicio.equals(DESDE)).as("semana inicia lunes").isTrue();
        });
    }

    @Test
    void ventasPorLineaSumaLoMismoQuePorVendedor() throws Exception {
        BigDecimal totalVendedores = total(queries.ventasPorVendedor(DESDE, HASTA, null));
        Map<String, Object> porLinea = queries.ventasPorLinea(DESDE, HASTA, null, false);

        assertThat(total(porLinea)).isEqualByComparingTo(totalVendedores);
        assertThat(total(queries.ventasPorLinea(DESDE, HASTA, null, true))).isEqualByComparingTo(totalVendedores);
        assertThat(filas(porLinea)).extracting(f -> f.get("LineaNegocio")).contains("conduit", "celdas y tableros");
    }

    @Test
    void filtroPorVendedorDevuelveSoloEseVendedor() throws Exception {
        Map<String, Object> primero = filas(queries.ventasPorVendedor(DESDE, HASTA, null)).get(0);
        String codigo = String.valueOf(primero.get("CodigoVendedor"));

        Map<String, Object> filtrado = queries.ventasPorVendedor(DESDE, HASTA, codigo);

        assertThat(filas(filtrado)).hasSize(1);
        assertThat(total(filtrado)).isEqualByComparingTo(new BigDecimal(primero.get("Valor").toString()));
    }

    @Test
    void informeSemanalCuadraConVentasPorVendedor() throws Exception {
        Map<String, Object> informe = queries.informeSemanal(DESDE, HASTA);
        Map<String, Object> porVendedor = queries.ventasPorVendedor(DESDE, HASTA, null);

        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> totales = (Map<String, BigDecimal>) informe.get("totales_periodo");
        List<Map<String, Object>> resumen = filas(Map.of("filas", informe.get("resumen_por_vendedor")));
        assertThat(totales.get("Total")).isEqualByComparingTo(total(porVendedor));
        assertThat(totales.get("TotalAprobado").add(totales.get("TotalEnRevision")).add(totales.get("TotalDetenido")))
                .as("el total es la suma de los estados no cancelados/anulados")
                .isEqualByComparingTo(totales.get("Total"));
        assertThat(resumen).as("incluye a los vendedores sin ventas").hasSizeGreaterThanOrEqualTo(filas(porVendedor).size());
        assertThat(resumen).extracting(f -> String.valueOf(f.get("Vendedor")))
                .as("nombres sin CR/LF").noneMatch(nombre -> nombre.contains("\r") || nombre.contains("\n"));
        for (String seccion : List.of("ventas_por_dia", "pedidos")) {
            assertThat(filas(Map.of("filas", informe.get(seccion))).stream()
                    .map(f -> new BigDecimal(f.get("Total").toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add))
                    .as(seccion).isEqualByComparingTo(totales.get("Total"));
        }
        assertThat(filas(Map.of("filas", informe.get("referencias_repetidas"))))
                .allSatisfy(f -> assertThat(((Number) f.get("Veces")).intValue()).isGreaterThan(1));
    }

    @Test
    void fechaFinalAnteriorALaInicialEsError() {
        assertThatThrownBy(() -> queries.ventasPorVendedor(HASTA, DESDE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void detalleDePedidoIncluyeOrdenDeCompra() throws Exception {
        Map<String, Object> pedido = queries.consultarPedido(49594);

        @SuppressWarnings("unchecked")
        Map<String, Object> encabezado = (Map<String, Object>) pedido.get("encabezado");
        assertThat(encabezado).containsKeys("OrdenCompra", "Vendedor", "Cliente", "NIT");
        assertThat(filas(Map.of("filas", pedido.get("referencias"))))
                .allSatisfy(linea -> assertThat(linea).containsKeys("Cantidad", "ValorUnitario", "ValorTotal", "Estado", "Proyecto"));
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> filas(Map<String, Object> resultado) {
        return (List<Map<String, Object>>) resultado.get("filas");
    }

    private static BigDecimal total(Map<String, Object> reporte) {
        return (BigDecimal) reporte.get("valor_total_sin_impuestos");
    }
}
