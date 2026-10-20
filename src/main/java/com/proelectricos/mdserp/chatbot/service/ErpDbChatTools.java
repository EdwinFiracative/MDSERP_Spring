package com.proelectricos.mdserp.chatbot.service;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Tool;
import com.google.genai.types.Type;
import com.proelectricos.mdserp.chatbot.erpdb.ErpDbReadOnlyQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

/**
 * Herramientas (function calling) que el modelo puede usar. Todas consultan únicamente ErpDb.
 * Las preguntas frecuentes tienen su propia herramienta con SQL fijo: responden en una sola llamada y sin errores de SQL.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class ErpDbChatTools {

    static final String CONSULTAR_PEDIDO = "consultar_pedido";
    static final String VENTAS_POR_VENDEDOR = "ventas_por_vendedor";
    static final String VENTAS_POR_SEMANA = "ventas_por_semana";
    static final String VENTAS_POR_LINEA = "ventas_por_linea_negocio";
    static final String INFORME_SEMANAL = "informe_semanal_ventas";
    static final String CONSULTAR_SQL = "consultar_sql";

    private static final Schema FECHA_INICIAL = texto("Fecha inicial incluida, formato YYYY-MM-DD (fecha del pedido)");
    private static final Schema FECHA_FINAL = texto("Fecha final incluida, formato YYYY-MM-DD (fecha del pedido)");
    private static final Schema VENDEDOR = texto("Opcional: código exacto o parte del nombre del vendedor para filtrar");

    private final ErpDbReadOnlyQueries queries;

    public Tool tool() {
        FunctionDeclaration consultarPedido = FunctionDeclaration.builder()
                .name(CONSULTAR_PEDIDO)
                .description("Detalle completo de un pedido de ErpDb por su número: encabezado (cliente, sede, NIT, "
                        + "ciudad, vendedor, orden de compra, condiciones, notas), referencias con cantidad, valor unitario, "
                        + "valor total, estado, proyecto y fecha de entrega, y total del pedido.")
                .parameters(objeto(Map.of("numero", Schema.builder()
                        .type(Type.Known.INTEGER)
                        .description("Número del pedido (OrderHeader.orderHeaderNumber)")
                        .build()), "numero"))
                .build();

        FunctionDeclaration ventasPorVendedor = FunctionDeclaration.builder()
                .name(VENTAS_POR_VENDEDOR)
                .description("Total de pedidos (cantidad) y valor vendido por vendedor en un rango de fechas, "
                        + "ordenado de mayor a menor valor, con el valor total del periodo.")
                .parameters(objeto(Map.of("fecha_inicial", FECHA_INICIAL, "fecha_final", FECHA_FINAL,
                        "vendedor", VENDEDOR), "fecha_inicial", "fecha_final"))
                .build();

        FunctionDeclaration ventasPorSemana = FunctionDeclaration.builder()
                .name(VENTAS_POR_SEMANA)
                .description("Ventas de un mes por vendedor y por semana (lunes a domingo, recortadas al mes): pedidos y "
                        + "valor de cada semana, total por vendedor y total del mes.")
                .parameters(objeto(Map.of(
                        "anio", Schema.builder().type(Type.Known.INTEGER).description("Año, ej. 2026").build(),
                        "mes", Schema.builder().type(Type.Known.INTEGER).description("Mes de 1 a 12").build(),
                        "vendedor", VENDEDOR), "anio", "mes"))
                .build();

        FunctionDeclaration ventasPorLinea = FunctionDeclaration.builder()
                .name(VENTAS_POR_LINEA)
                .description("Total de pedidos y valor vendido por línea de negocio (conduit, celdas y tableros, "
                        + "comercialización, servicios industriales, MDS Ingeniería, ingresos no operacionales, Otros) "
                        + "en un rango de fechas; opcionalmente desglosado por vendedor.")
                .parameters(objeto(Map.of("fecha_inicial", FECHA_INICIAL, "fecha_final", FECHA_FINAL,
                        "vendedor", VENDEDOR,
                        "por_vendedor", Schema.builder().type(Type.Known.BOOLEAN)
                                .description("true para desglosar cada línea de negocio por vendedor").build()),
                        "fecha_inicial", "fecha_final"))
                .build();

        FunctionDeclaration informeSemanal = FunctionDeclaration.builder()
                .name(INFORME_SEMANAL)
                .description("Informe semanal de ventas por vendedor: resumen de TODOS los vendedores (pedidos, clientes, "
                        + "líneas, unidades, total, participación, promedio por pedido y total por estado), ventas por día, "
                        + "detalle de pedidos y referencias repetidas en un mismo pedido (posible doble digitación). "
                        + "Sin fechas devuelve la semana anterior completa (lunes a domingo).")
                .parameters(objeto(Map.of(
                        "fecha_inicial", texto("Opcional: primer día del periodo, formato YYYY-MM-DD (fecha del pedido)"),
                        "fecha_final", texto("Opcional: último día del periodo incluido, formato YYYY-MM-DD"))))
                .build();

        FunctionDeclaration consultarSql = FunctionDeclaration.builder()
                .name(CONSULTAR_SQL)
                .description("Ejecuta UNA consulta T-SQL de solo lectura (SELECT o WITH) en la base ErpDb de SQL Server "
                        + "y devuelve columnas y filas. Úsala solo si ninguna otra herramienta responde la pregunta. "
                        + "No se permite modificar datos ni consultar otras bases.")
                .parameters(objeto(Map.of("consulta", texto("Consulta T-SQL SELECT con TOP para limitar resultados")),
                        "consulta"))
                .build();

        return Tool.builder()
                .functionDeclarations(consultarPedido, ventasPorVendedor, ventasPorSemana, ventasPorLinea, informeSemanal,
                        consultarSql)
                .build();
    }

    /** Ejecuta la función pedida por el modelo; los errores se devuelven al modelo para que se corrija. */
    public Map<String, Object> ejecutar(FunctionCall llamada) {
        String nombre = llamada.name().orElse("");
        Map<String, Object> args = llamada.args().orElse(Map.of());
        log.info("Chatbot -> {} {}", nombre, args);
        long inicio = System.nanoTime();
        try {
            return switch (nombre) {
                case CONSULTAR_PEDIDO -> queries.consultarPedido(entero(args.get("numero"), "número de pedido"));
                case VENTAS_POR_VENDEDOR -> queries.ventasPorVendedor(
                        fecha(args.get("fecha_inicial")), fecha(args.get("fecha_final")), cadena(args.get("vendedor")));
                case VENTAS_POR_SEMANA -> {
                    YearMonth mes = mes(args.get("anio"), args.get("mes"));
                    yield queries.ventasPorSemana(mes.atDay(1), mes.atEndOfMonth(), cadena(args.get("vendedor")));
                }
                case VENTAS_POR_LINEA -> queries.ventasPorLinea(
                        fecha(args.get("fecha_inicial")), fecha(args.get("fecha_final")), cadena(args.get("vendedor")),
                        Boolean.parseBoolean(String.valueOf(args.get("por_vendedor"))));
                case INFORME_SEMANAL -> {
                    LocalDate[] periodo = periodoInforme(args.get("fecha_inicial"), args.get("fecha_final"));
                    yield queries.informeSemanal(periodo[0], periodo[1]);
                }
                case CONSULTAR_SQL -> queries.consultarSql(String.valueOf(args.get("consulta")));
                default -> Map.of("error", "Función desconocida: " + nombre);
            };
        } catch (IllegalArgumentException e) {
            return Map.of("error", e.getMessage());
        } catch (SQLException e) {
            log.warn("Chatbot: error SQL en {}: {}", nombre, e.getMessage());
            return Map.of("error", "Error de SQL Server: " + e.getMessage());
        } finally {
            log.info("Chatbot <- {} en {} ms", nombre, (System.nanoTime() - inicio) / 1_000_000);
        }
    }

    private static Schema objeto(Map<String, Schema> propiedades, String... requeridos) {
        return Schema.builder().type(Type.Known.OBJECT).properties(propiedades).required(List.of(requeridos)).build();
    }

    private static Schema texto(String descripcion) {
        return Schema.builder().type(Type.Known.STRING).description(descripcion).build();
    }

    private static int entero(Object valor, String que) {
        if (valor instanceof Number numero) {
            return numero.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(valor).trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El " + que + " no es válido: " + valor);
        }
    }

    private static LocalDate fecha(Object valor) {
        try {
            return LocalDate.parse(String.valueOf(valor).trim());
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Fecha no válida (usa YYYY-MM-DD): " + valor);
        }
    }

    // Sin fechas: la semana anterior completa, de lunes a domingo
    private static LocalDate[] periodoInforme(Object inicial, Object fin) {
        boolean sinInicial = inicial == null || String.valueOf(inicial).isBlank();
        boolean sinFinal = fin == null || String.valueOf(fin).isBlank();
        if (sinInicial && sinFinal) {
            LocalDate lunes = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
            return new LocalDate[] {lunes, lunes.plusDays(6)};
        }
        if (sinInicial || sinFinal) {
            throw new IllegalArgumentException("Indica fecha_inicial y fecha_final, o ninguna para la semana anterior");
        }
        return new LocalDate[] {fecha(inicial), fecha(fin)};
    }

    private static YearMonth mes(Object anio, Object mes) {
        try {
            return YearMonth.of(entero(anio, "año"), entero(mes, "mes"));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Mes no válido: " + anio + "-" + mes);
        }
    }

    private static String cadena(Object valor) {
        return valor == null ? null : String.valueOf(valor);
    }
}
