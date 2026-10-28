package com.proelectricos.mdserp.erpdb.order.biconnector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.hamcrest.Matchers.startsWith;

class BitrixAppTokenServiceTest {

    private static final String OAUTH = "https://oauth.test/oauth/token/";

    @TempDir
    Path dir;

    private MockRestServiceServer oauth;
    private RestClient.Builder builder;
    private Path tokenFile;

    @BeforeEach
    void setUp() {
        builder = RestClient.builder().baseUrl(OAUTH);
        oauth = MockRestServiceServer.bindTo(builder).build();
        tokenFile = dir.resolve("data/token.txt");
    }

    private BitrixAppTokenService service(String clientSecret) {
        return new BitrixAppTokenService(builder, "local.abc", clientSecret, tokenFile);
    }

    @Test
    void renuevaElTokenGuardadoYGuardaElNuevo() throws IOException {
        BitrixAppTokenService service = service("secreto");
        service.saveRefreshToken("ref-1");
        oauth.expect(requestTo(startsWith(OAUTH))).andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("grant_type", "refresh_token"))
                .andExpect(queryParam("client_id", "local.abc"))
                .andExpect(queryParam("client_secret", "secreto"))
                .andExpect(queryParam("refresh_token", "ref-1"))
                .andRespond(withSuccess("{\"access_token\":\"acc-2\",\"refresh_token\":\"ref-2\"}",
                        MediaType.APPLICATION_JSON));

        assertThat(service.refreshAccessToken()).contains("acc-2");

        oauth.verify();
        assertThat(Files.readString(tokenFile)).isEqualTo("ref-2");
    }

    @Test
    void sinTokenGuardadoNoLlamaABitrix() {
        assertThat(service("secreto").refreshAccessToken()).isEmpty();
        oauth.verify();
    }

    @Test
    void tokenRechazadoDevuelveVacioYConservaElGuardado() throws IOException {
        BitrixAppTokenService service = service("secreto");
        service.saveRefreshToken("ref-1");
        oauth.expect(requestTo(startsWith(OAUTH)))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"invalid_grant\"}"));

        assertThat(service.refreshAccessToken()).isEmpty();
        assertThat(Files.readString(tokenFile)).isEqualTo("ref-1");
    }

    @Test
    void sinClientSecretNoGuardaNiRenueva() {
        BitrixAppTokenService service = service("");
        service.saveRefreshToken("ref-1");

        assertThat(service.isConfigured()).isFalse();
        assertThat(tokenFile).doesNotExist();
        assertThat(service.refreshAccessToken()).isEmpty();
    }
}
