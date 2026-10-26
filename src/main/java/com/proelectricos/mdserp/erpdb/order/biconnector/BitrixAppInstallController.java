package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.BitrixApiException;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.InvalidAppTokenException;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Callback de instalacion de la aplicacion local de Bitrix24 ("Initial installation path"): Bitrix24 hace
 * un POST con auth[access_token] y auth[refresh_token] justo despues de agregar la aplicacion, y con ese
 * token se registra el conector BI ({@link BiConnectorRegistrationService}). Reinstalar la aplicacion, o
 * abrirla si su "Handler path" apunta aqui, vuelve a ejecutar el registro (y agrega las columnas nuevas).
 * Los tokens no se guardan: solo se usan durante esta llamada.
 * Ruta publica: {B24_APP_PUBLIC_BASE_URL}/bitrix-app/install.
 * Ver https://apidocs.bitrix24.com/settings/app-installation/local-apps/installation-callback.html
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/bitrix-app")
public class BitrixAppInstallController {

    public static final String INSTALL_PATH = "/bitrix-app/install";

    private final BiConnectorRegistrationService registrationService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/install", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> install(HttpServletRequest request) throws IOException {
        Map<String, Object> params = params(request);
        // Solo los nombres de los parametros: los valores traen tokens
        log.info("Instalacion app Bitrix24 desde {}: parametros {}", request.getRemoteAddr(), params.keySet());
        String accessToken = accessToken(params);
        if (accessToken == null) {
            log.warn("Instalacion app Bitrix24 sin token (ni auth[access_token] ni AUTH_ID): no se registra nada");
            return error(HttpStatus.BAD_REQUEST, "AUTH_REQUIRED", "Auth data is required");
        }
        try {
            Result result = registrationService.register(accessToken);
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", "success");
            body.put("connectorId", result.connectorId());
            body.put("sourceId", result.sourceId());
            body.put("createdTables", result.createdTables());
            body.put("existingTables", result.existingTables());
            body.put("addedColumns", result.addedColumns());
            return ResponseEntity.ok(body);
        } catch (InvalidAppTokenException e) {
            log.warn("Instalacion app Bitrix24 rechazada desde {}: {}", request.getRemoteAddr(), e.getMessage());
            return error(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", e.getMessage());
        } catch (BitrixApiException e) {
            log.error("Instalacion app Bitrix24: fallo el registro del conector BI: {}", e.getMessage());
            return error(HttpStatus.BAD_GATEWAY, e.getError(), e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Instalacion app Bitrix24: {}", e.getMessage());
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "CONFIGURATION_ERROR", e.getMessage());
        }
    }

    // La documentacion muestra el cuerpo como formulario (auth[access_token]=...); se acepta tambien JSON
    private Map<String, Object> params(HttpServletRequest request) throws IOException {
        if (request.getContentType() != null && request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            try (InputStream body = request.getInputStream()) {
                return objectMapper.readValue(body, new TypeReference<>() {
                });
            }
        }
        return FormParams.parse(request.getParameterMap());
    }

    /*
     * El callback servidor a servidor de la primera instalacion manda auth[access_token]; cuando Bitrix24
     * abre la URL en el iframe de la app (reinstalar, abrir la app) manda el token como AUTH_ID.
     */
    private static String accessToken(Map<String, Object> params) {
        if (params.get("auth") instanceof Map<?, ?> auth && auth.get("access_token") instanceof String token
                && !token.isBlank()) {
            return token;
        }
        return params.get("AUTH_ID") instanceof String token && !token.isBlank() ? token : null;
    }

    private static ResponseEntity<Map<String, Object>> error(HttpStatus status, String error, String description) {
        return ResponseEntity.status(status).body(Map.of("status", "error", "error", error, "error_description", description));
    }
}
