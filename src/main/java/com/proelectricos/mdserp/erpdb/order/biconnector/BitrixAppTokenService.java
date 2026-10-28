package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Guarda el refresh_token de la aplicacion local de Bitrix24 (llega al instalarla o abrirla) y lo cambia por
 * un access_token cuando el backend necesita llamar a biconnector.* sin que nadie abra la app.
 * Bitrix24 entrega un refresh_token nuevo en cada renovacion: se reemplaza el guardado.
 * Ver https://apidocs.bitrix24.com/settings/oauth/index.html
 */
@Slf4j
@Service
public class BitrixAppTokenService {

    private final RestClient oauthClient;
    private final String clientId;
    private final String clientSecret;
    private final Path tokenFile;

    @Autowired
    public BitrixAppTokenService(@Value("${b24.oauth-url:https://oauth.bitrix.info/oauth/token/}") String oauthUrl,
                                 @Value("${b24.app.client-id:}") String clientId,
                                 @Value("${b24.app.client-secret:}") String clientSecret,
                                 @Value("${b24.app.token-file:data/bitrix-app-token.txt}") String tokenFile) {
        this(RestClient.builder().baseUrl(oauthUrl), clientId, clientSecret, Path.of(tokenFile));
    }

    BitrixAppTokenService(RestClient.Builder oauthClient, String clientId, String clientSecret, Path tokenFile) {
        this.oauthClient = oauthClient.build();
        this.clientId = clientId.strip();
        this.clientSecret = clientSecret.strip();
        this.tokenFile = tokenFile;
    }

    /** Sin client_id y client_secret no se puede renovar el token: no tiene sentido guardarlo. */
    public boolean isConfigured() {
        return !clientId.isEmpty() && !clientSecret.isEmpty();
    }

    public void saveRefreshToken(String refreshToken) {
        if (!isConfigured() || refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        try {
            Path dir = tokenFile.toAbsolutePath().getParent();
            Files.createDirectories(dir);
            // Se escribe a un temporal y se mueve, para no dejar el archivo a medias
            Path tmp = Files.createTempFile(dir, "bitrix-app-token", ".tmp");
            Files.writeString(tmp, refreshToken.strip(), StandardCharsets.UTF_8);
            Files.move(tmp, tokenFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el token de la app Bitrix24 en " + tokenFile, e);
        }
    }

    /** access_token nuevo a partir del refresh_token guardado; vacio si no hay token o Bitrix24 lo rechaza. */
    public Optional<String> refreshAccessToken() {
        if (!isConfigured()) {
            return Optional.empty();
        }
        Optional<String> refreshToken = readRefreshToken();
        if (refreshToken.isEmpty()) {
            log.info("App Bitrix24: no hay token guardado en {}; se guarda al abrir o reinstalar la app", tokenFile);
            return Optional.empty();
        }
        JsonNode response;
        try {
            response = oauthClient.get()
                    .uri(uri -> uri.queryParam("grant_type", "refresh_token")
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecret)
                            .queryParam("refresh_token", refreshToken.get())
                            .build())
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientException e) {
            log.warn("App Bitrix24: no se pudo renovar el token: {}", e.getMessage());
            return Optional.empty();
        }
        String accessToken = response == null ? "" : response.path("access_token").asText();
        if (accessToken.isEmpty()) {
            log.warn("App Bitrix24: la renovacion del token no devolvio access_token: {}",
                    response == null ? "respuesta vacia" : response.path("error").asText());
            return Optional.empty();
        }
        saveRefreshToken(response.path("refresh_token").asText());
        return Optional.of(accessToken);
    }

    private Optional<String> readRefreshToken() {
        try {
            if (!Files.isRegularFile(tokenFile)) {
                return Optional.empty();
            }
            String token = Files.readString(tokenFile, StandardCharsets.UTF_8).strip();
            return token.isEmpty() ? Optional.empty() : Optional.of(token);
        } catch (IOException e) {
            log.warn("App Bitrix24: no se pudo leer {}: {}", tokenFile, e.getMessage());
            return Optional.empty();
        }
    }
}
