package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorFilter.DateRange;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiTable.Column;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Endpoints del conector BI de Bitrix24 BI Builder (biconnector.connector.add: urlCheck, urlTableList,
 * urlTableDescription, urlData). Bitrix24 los llama por POST con cuerpo form-urlencoded; la clave la valida
 * {@link BiConnectorKeyFilter}. Detras de nginx la ruta publica es {B24_APP_PUBLIC_BASE_URL}/bi-connector/...
 * Ver https://apidocs.bitrix24.com/api-reference/biconnector/connector/index.html
 */
@Slf4j
@RestController
@RequestMapping("/bi-connector")
public class BiConnectorController {

    private static final String JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

    private final BiConnectorTables tables;
    private final ObjectMapper objectMapper;
    private final int defaultWindowDays;
    private final Clock clock;

    @Autowired
    public BiConnectorController(BiConnectorTables tables, ObjectMapper objectMapper,
                                 @Value("${b24.connector-default-window-days:90}") int defaultWindowDays) {
        // "Hoy" en hora de Colombia, aunque el contenedor corra en UTC
        this(tables, objectMapper, defaultWindowDays, Clock.system(ZoneId.of("America/Bogota")));
    }

    BiConnectorController(BiConnectorTables tables, ObjectMapper objectMapper, int defaultWindowDays, Clock clock) {
        this.tables = tables;
        this.objectMapper = objectMapper;
        this.defaultWindowDays = defaultWindowDays;
        this.clock = clock;
    }

    // Bitrix24 solo exige 200 y cuerpo no vacio
    @PostMapping(value = "/check", produces = MediaType.TEXT_PLAIN_VALUE)
    public String check() {
        return "OK";
    }

    @PostMapping(value = "/table_list", produces = JSON_UTF8)
    public List<Map<String, String>> tableList(@RequestParam(required = false) String searchString) {
        String search = searchString == null ? "" : searchString.strip().toLowerCase(Locale.ROOT);
        return tables.all().stream()
                .filter(table -> table.code().contains(search) || table.title().toLowerCase(Locale.ROOT).contains(search))
                .map(table -> Map.of("code", table.code(), "title", table.title()))
                .toList();
    }

    @PostMapping(value = "/table_description", produces = JSON_UTF8)
    public List<Map<String, String>> tableDescription(@RequestParam String table) {
        BiTable<?> found = findTable(table);
        log.info("Conector BI: table_description {} ({} columnas)", found.code(), found.columns().size());
        return found.columns().stream()
                .map(column -> Map.of("code", column.code(), "name", column.name(), "type", column.type().code()))
                .toList();
    }

    /**
     * Filas de la tabla como arreglo de arreglos: el primero son los codigos de campo y los demas las filas.
     * Se consulta por meses dentro del rango de fecha_pedido del filtro (o la ventana por defecto si no trae)
     * y se escribe en streaming. Orden: mes, numero de pedido, posicion.
     */
    @PostMapping("/data")
    public void data(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> params = FormParams.parse(request.getParameterMap());
        BiTable<?> table = findTable(params.get("table") instanceof String code ? code : null);
        BiConnectorFilter filter = BiConnectorFilter.parse(params.get("filter")).withoutInvalidDates(table.dateColumnCodes());
        Integer limit = parseLimit(params.get("limit"));
        DateRange range = window(filter.dateRange(BiConnectorTables.DATE_COLUMN));
        List<String> select = selectedColumns(table, FormParams.values(params.get("select")), params.get("mapFields"));
        log.info("Conector BI: data {} select {} mapFields {} filter {} limit {}", table.code(),
                params.get("select"), params.get("mapFields"), params.get("filter"), limit);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(JSON_UTF8);
        try (JsonGenerator json = objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8)) {
            json.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
            json.writeStartArray();
            json.writeStartArray();
            for (String code : select) {
                json.writeString(code);
            }
            json.writeEndArray();
            int written = writeRows(json, table, filter, select, range, limit);
            json.writeEndArray();
            log.info("Conector BI: {} {} filas ({} a {}, limit {})", table.code(), written, range.from(), range.to(), limit);
        }
    }

    private <R> int writeRows(JsonGenerator json, BiTable<R> table, BiConnectorFilter filter, List<String> select,
                              DateRange range, Integer limit) throws IOException {
        Map<String, Column<R>> columns = table.columnsByCode();
        Set<String> codes = columns.keySet();
        int written = 0;
        for (LocalDate monthStart = range.from(); !monthStart.isAfter(range.to());
             monthStart = monthStart.with(TemporalAdjusters.firstDayOfNextMonth())) {
            LocalDate monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());
            for (R row : table.loader().apply(monthStart, monthEnd.isAfter(range.to()) ? range.to() : monthEnd)) {
                if (limit != null && written >= limit) {
                    return written;
                }
                if (!filter.test(codes, code -> columns.get(code).value().apply(row))) {
                    continue;
                }
                json.writeStartArray();
                for (String code : select) {
                    Column<R> column = columns.get(code);
                    writeValue(json, column == null ? null : column.value().apply(row));
                }
                json.writeEndArray();
                written++;
            }
            json.flush();
        }
        return written;
    }

    private static void writeValue(JsonGenerator json, Object value) throws IOException {
        if (value == null) {
            json.writeNull();
        } else if (value instanceof BigDecimal decimal) {
            json.writeNumber(decimal);
        } else if (value instanceof Long number) {
            json.writeNumber(number);
        } else if (value instanceof Integer number) {
            json.writeNumber(number);
        } else {
            // LocalDate.toString() es yyyy-MM-dd
            json.writeString(value.toString());
        }
    }

    // Rango del filtro completado con la ventana por defecto que termina hoy
    private DateRange window(DateRange filtered) {
        LocalDate to = filtered.to() != null ? filtered.to() : LocalDate.now(clock);
        LocalDate from = filtered.from() != null ? filtered.from() : to.minusDays(defaultWindowDays);
        return new DateRange(from, to);
    }

    /**
     * Columnas a devolver, en el orden pedido. Bitrix24 exige exactamente tantas columnas como pide: puede
     * pedirlas en select (con el nombre en mayusculas de table.add o con el codigo) o solo en mapFields
     * (codigo externo => nombre). Sin ninguno de los dos, todas. Una columna desconocida sale en null.
     */
    private static List<String> selectedColumns(BiTable<?> table, List<String> select, Object mapFields) {
        List<String> requested = select.isEmpty() && mapFields instanceof Map<?, ?> map
                ? map.keySet().stream().map(String::valueOf).toList()
                : select;
        if (requested.isEmpty()) {
            return table.columns().stream().map(Column::code).toList();
        }
        return requested.stream().map(code -> code.strip().toLowerCase(Locale.ROOT)).distinct().toList();
    }

    private static Integer parseLimit(Object limit) {
        if (!(limit instanceof String text) || text.isBlank()) {
            return null;
        }
        try {
            int value = Integer.parseInt(text.strip());
            return value > 0 ? value : null;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit invalido: " + text);
        }
    }

    private BiTable<?> findTable(String code) {
        return tables.find(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tabla desconocida: " + code));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleStatus(ResponseStatusException e) {
        log.warn("Conector BI: {}", e.getReason());
        return ResponseEntity.status(e.getStatusCode()).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", HttpStatus.valueOf(e.getStatusCode().value()).name(),
                        "error_description", String.valueOf(e.getReason())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpected(Exception e) {
        log.error("Conector BI: error atendiendo a Bitrix24", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", "INTERNAL_ERROR", "error_description", "Error interno del conector"));
    }
}
