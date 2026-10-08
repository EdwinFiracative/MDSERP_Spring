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
import java.util.Map;

/**
 * Herramientas (function calling) que el modelo puede usar. Todas consultan únicamente ErpDb.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class ErpDbChatTools {

    static final String CONSULTAR_PEDIDO = "consultar_pedido";
    static final String CONSULTAR_SQL = "consultar_sql";

    private final ErpDbReadOnlyQueries queries;

    public Tool tool() {
        FunctionDeclaration consultarPedido = FunctionDeclaration.builder()
                .name(CONSULTAR_PEDIDO)
                .description("Detalle completo de un pedido de ErpDb por su número: encabezado (cliente, sede, NIT, "
                        + "ciudad, vendedor, condiciones, notas), referencias con estado, proyecto y fecha de entrega, y total.")
                .parameters(Schema.builder()
                        .type(Type.Known.OBJECT)
                        .properties(Map.of("numero", Schema.builder()
                                .type(Type.Known.INTEGER)
                                .description("Número del pedido (OrderHeader.orderHeaderNumber)")
                                .build()))
                        .required("numero"))
                .build();

        FunctionDeclaration consultarSql = FunctionDeclaration.builder()
                .name(CONSULTAR_SQL)
                .description("Ejecuta UNA consulta T-SQL de solo lectura (SELECT o WITH) en la base ErpDb de SQL Server "
                        + "y devuelve columnas y filas. No se permite modificar datos ni consultar otras bases.")
                .parameters(Schema.builder()
                        .type(Type.Known.OBJECT)
                        .properties(Map.of("consulta", Schema.builder()
                                .type(Type.Known.STRING)
                                .description("Consulta T-SQL SELECT con TOP para limitar resultados")
                                .build()))
                        .required("consulta"))
                .build();

        return Tool.builder().functionDeclarations(consultarPedido, consultarSql).build();
    }

    /** Ejecuta la función pedida por el modelo; los errores se devuelven al modelo para que se corrija. */
    public Map<String, Object> ejecutar(FunctionCall llamada) {
        String nombre = llamada.name().orElse("");
        Map<String, Object> args = llamada.args().orElse(Map.of());
        log.info("Chatbot -> {} {}", nombre, args);
        try {
            return switch (nombre) {
                case CONSULTAR_PEDIDO -> queries.consultarPedido(entero(args.get("numero")));
                case CONSULTAR_SQL -> queries.consultarSql(String.valueOf(args.get("consulta")));
                default -> Map.of("error", "Función desconocida: " + nombre);
            };
        } catch (IllegalArgumentException e) {
            return Map.of("error", e.getMessage());
        } catch (SQLException e) {
            log.warn("Chatbot: error SQL en {}: {}", nombre, e.getMessage());
            return Map.of("error", "Error de SQL Server: " + e.getMessage());
        }
    }

    private static int entero(Object valor) {
        if (valor instanceof Number numero) {
            return numero.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(valor).trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El número de pedido no es válido: " + valor);
        }
    }
}
