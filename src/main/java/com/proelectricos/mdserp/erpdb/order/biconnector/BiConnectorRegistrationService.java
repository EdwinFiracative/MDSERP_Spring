package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Registra en Bitrix24 el conector BI, su fuente y sus tablas con el token OAuth que recibe la instalacion
 * de la aplicacion local ({@link BitrixAppInstallController}). Los metodos biconnector.connector/source/table
 * no admiten webhook, solo contexto de aplicacion. Es idempotente: si ya existen (reinstalacion) se
 * actualizan las URL y la clave, y solo se crean las tablas que falten.
 * Ver https://apidocs.bitrix24.com/api-reference/biconnector/index.html
 */
@Slf4j
@Service
public class BiConnectorRegistrationService {

    static final String CONNECTOR_TITLE = "MDSERP Pedidos";
    static final String SOURCE_TITLE = "MDSERP ErpDb";
    // Los nombres de tabla son unicos en todo el portal: se prefijan para no chocar con otros
    static final String TABLE_PREFIX = "mdserp_";
    private static final int PAGE_SIZE = 50;
    private static final String LOGO = logo();

    /** El token no es valido para el portal configurado (o es de otra aplicacion). */
    public static class InvalidAppTokenException extends RuntimeException {
        InvalidAppTokenException(String message) {
            super(message);
        }
    }

    /** Bitrix24 respondio con error a un metodo REST. */
    public static class BitrixApiException extends RuntimeException {
        private final String error;

        BitrixApiException(String method, String error, String description) {
            super(method + ": " + error + " - " + description);
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public record Result(long connectorId, long sourceId, List<String> createdTables, List<String> existingTables) {
    }

    private final RestClient client;
    private final BiConnectorTables tables;
    private final ObjectMapper objectMapper;
    private final String publicBaseUrl;
    private final String connectorKey;
    private final String appClientId;

    public BiConnectorRegistrationService(@Qualifier("bitrixAppRestClient") RestClient client,
                                          BiConnectorTables tables, ObjectMapper objectMapper,
                                          @Value("${b24.app-public-base-url:}") String publicBaseUrl,
                                          @Value("${b24.connector-key:}") String connectorKey,
                                          @Value("${b24.app.client-id:}") String appClientId) {
        this.client = client;
        this.tables = tables;
        this.objectMapper = objectMapper;
        this.publicBaseUrl = publicBaseUrl.strip().replaceAll("/+$", "");
        this.connectorKey = connectorKey.strip();
        this.appClientId = appClientId.strip();
    }

    public Result register(String accessToken) {
        if (publicBaseUrl.isEmpty() || connectorKey.isEmpty()) {
            throw new IllegalStateException("Faltan B24_APP_PUBLIC_BASE_URL o B24_CONNECTOR_KEY en la configuracion");
        }
        verifyToken(accessToken);
        long connectorId = upsertConnector(accessToken);
        // Al crear la fuente Bitrix24 llama a urlCheck con la clave: el backend ya debe estar desplegado
        long sourceId = upsertSource(accessToken, connectorId);

        Set<String> existingCodes = new HashSet<>();
        for (JsonNode table : list(accessToken, "biconnector.table.list", Map.of("sourceId", sourceId))) {
            existingCodes.add(table.path("externalCode").asText());
        }
        List<String> created = new ArrayList<>();
        List<String> existing = new ArrayList<>();
        for (BiTable<?> table : tables.all()) {
            if (existingCodes.contains(table.code())) {
                existing.add(table.code());
            } else {
                call(accessToken, "biconnector.table.add", Map.of("fields", tableFields(table, sourceId)));
                created.add(table.code());
            }
        }
        log.info("Conector BI registrado: conector {}, fuente {}, tablas creadas {}, ya existentes {}",
                connectorId, sourceId, created, existing);
        return new Result(connectorId, sourceId, created, existing);
    }

    // El token se valida contra el portal configurado (nunca contra el dominio que llega en la peticion)
    private void verifyToken(String accessToken) {
        JsonNode app;
        try {
            app = call(accessToken, "app.info", Map.of());
        } catch (BitrixApiException e) {
            throw new InvalidAppTokenException("Token rechazado por Bitrix24: " + e.getError());
        }
        if (!appClientId.isEmpty() && !appClientId.equals(app.path("CODE").asText())) {
            throw new InvalidAppTokenException("El token es de otra aplicacion: " + app.path("CODE").asText());
        }
    }

    private long upsertConnector(String accessToken) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("title", CONNECTOR_TITLE);
        fields.put("description", "Pedidos de venta de ErpDb (lineas y notas) servidos por el backend MDSERP");
        fields.put("logo", LOGO);
        fields.put("urlCheck", publicBaseUrl + BiConnectorKeyFilter.PATH_PREFIX + "check");
        fields.put("urlTableList", publicBaseUrl + BiConnectorKeyFilter.PATH_PREFIX + "table_list");
        fields.put("urlTableDescription", publicBaseUrl + BiConnectorKeyFilter.PATH_PREFIX + "table_description");
        fields.put("urlData", publicBaseUrl + BiConnectorKeyFilter.PATH_PREFIX + "data");
        // settings se reemplaza completo en update: siempre se manda entero
        fields.put("settings", List.of(Map.of("code", BiConnectorKeyFilter.KEY_SETTING, "name", "Clave de acceso", "type", "STRING")));

        for (JsonNode connector : list(accessToken, "biconnector.connector.list", Map.of())) {
            if (CONNECTOR_TITLE.equals(connector.path("title").asText())) {
                long id = connector.path("id").asLong();
                call(accessToken, "biconnector.connector.update", Map.of("id", id, "fields", fields));
                return id;
            }
        }
        return call(accessToken, "biconnector.connector.add", Map.of("fields", fields)).path("id").asLong();
    }

    private long upsertSource(String accessToken, long connectorId) {
        Map<String, Object> settings = Map.of(BiConnectorKeyFilter.KEY_SETTING, connectorKey);
        for (JsonNode source : list(accessToken, "biconnector.source.list", Map.of("connectorId", connectorId))) {
            if (source.path("connectorId").asLong() == connectorId) {
                long id = source.path("id").asLong();
                call(accessToken, "biconnector.source.update", Map.of("id", id, "fields", Map.of("settings", settings)));
                return id;
            }
        }
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("title", SOURCE_TITLE);
        fields.put("description", "Pedidos de venta de ErpDb");
        fields.put("connectorId", connectorId);
        fields.put("settings", settings);
        return call(accessToken, "biconnector.source.add", Map.of("fields", fields)).path("id").asLong();
    }

    // Columnas: nombre en mayusculas (A-Z, 0-9, _; max 32) y externalCode = codigo que usa urlData
    private static Map<String, Object> tableFields(BiTable<?> table, long sourceId) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("sourceId", sourceId);
        fields.put("name", TABLE_PREFIX + table.code());
        fields.put("externalName", table.title());
        fields.put("externalCode", table.code());
        fields.put("fields", table.columns().stream()
                .map(column -> Map.of(
                        "name", column.code().toUpperCase(Locale.ROOT),
                        "externalCode", column.code(),
                        "type", column.type().code()))
                .toList());
        return fields;
    }

    // Los *.list devuelven un arreglo plano de hasta 50 elementos por pagina
    private List<JsonNode> list(String accessToken, String method, Map<String, Object> filter) {
        List<JsonNode> items = new ArrayList<>();
        for (int page = 1; ; page++) {
            Map<String, Object> body = new LinkedHashMap<>();
            if (!filter.isEmpty()) {
                body.put("filter", filter);
            }
            body.put("page", page);
            JsonNode result = call(accessToken, method, body);
            result.forEach(items::add);
            if (!result.isArray() || result.size() < PAGE_SIZE) {
                return items;
            }
        }
    }

    // Bitrix24 puede devolver el error en la raiz (HTTP 4xx/5xx) o dentro de "result" con HTTP 200
    private JsonNode call(String accessToken, String method, Map<String, Object> params) {
        Map<String, Object> body = new LinkedHashMap<>(params);
        body.put("auth", accessToken);
        JsonNode response = client.post().uri(method).body(body)
                .exchange((request, httpResponse) -> {
                    try (InputStream in = httpResponse.getBody()) {
                        return objectMapper.readTree(in);
                    }
                });
        if (response == null || response.isMissingNode()) {
            throw new BitrixApiException(method, "EMPTY_RESPONSE", "Respuesta vacia");
        }
        JsonNode error = response.has("error") ? response : response.path("result");
        if (error.hasNonNull("error") && !error.path("error").asText().isEmpty()) {
            throw new BitrixApiException(method, error.path("error").asText(), error.path("error_description").asText());
        }
        return response.path("result");
    }

    // Logo del conector (obligatorio): cuadro azul con barras, como data URI PNG
    private static String logo() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(0x1F4E79));
        graphics.fillRoundRect(0, 0, 64, 64, 14, 14);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(14, 36, 8, 16);
        graphics.fillRect(28, 24, 8, 28);
        graphics.fillRect(42, 12, 8, 40);
        graphics.dispose();
        try (ByteArrayOutputStream png = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", png);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(png.toByteArray());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
