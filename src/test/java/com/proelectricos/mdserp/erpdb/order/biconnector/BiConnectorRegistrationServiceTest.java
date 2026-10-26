package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.BitrixApiException;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.InvalidAppTokenException;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class BiConnectorRegistrationServiceTest {

    private static final String PORTAL = "https://portal.test/rest/";
    private static final String TOKEN = "token-de-prueba";
    private static final String KEY = "clave-de-prueba-0123456789abcdef";

    private MockRestServiceServer bitrix;
    private BiConnectorTables tables;
    private RestClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(PORTAL);
        bitrix = MockRestServiceServer.bindTo(builder).build();
        client = builder.build();
        tables = new BiConnectorTables(mock(BiOrderDatasetService.class));
    }

    private BiConnectorRegistrationService service(String clientId) {
        return new BiConnectorRegistrationService(client, tables, new ObjectMapper(),
                "https://erp.test/api/", KEY, clientId);
    }

    private void expect(String method, String response) {
        bitrix.expect(requestTo(PORTAL + method)).andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.auth").value(TOKEN))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
    }

    @Test
    void primeraInstalacionCreaConectorFuenteYTablas() {
        expect("app.info", "{\"result\":{\"ID\":1,\"CODE\":\"local.abc\"}}");
        expect("biconnector.connector.list", "{\"result\":[]}");
        bitrix.expect(requestTo(PORTAL + "biconnector.connector.add"))
                .andExpect(jsonPath("$.fields.title").value("MDSERP Pedidos"))
                .andExpect(jsonPath("$.fields.urlCheck").value("https://erp.test/api/bi-connector/check"))
                .andExpect(jsonPath("$.fields.urlTableList").value("https://erp.test/api/bi-connector/table_list"))
                .andExpect(jsonPath("$.fields.urlTableDescription").value("https://erp.test/api/bi-connector/table_description"))
                .andExpect(jsonPath("$.fields.urlData").value("https://erp.test/api/bi-connector/data"))
                .andExpect(jsonPath("$.fields.settings[0].code").value("key"))
                .andExpect(jsonPath("$.fields.settings[0].type").value("STRING"))
                .andExpect(jsonPath("$.fields.logo").value(startsWith("data:image/png;base64,")))
                .andRespond(withSuccess("{\"result\":{\"id\":4}}", MediaType.APPLICATION_JSON));
        bitrix.expect(requestTo(PORTAL + "biconnector.source.list"))
                .andExpect(jsonPath("$.filter.connectorId").value(4))
                .andRespond(withSuccess("{\"result\":[]}", MediaType.APPLICATION_JSON));
        bitrix.expect(requestTo(PORTAL + "biconnector.source.add"))
                .andExpect(jsonPath("$.fields.connectorId").value(4))
                .andExpect(jsonPath("$.fields.settings.key").value(KEY))
                .andRespond(withSuccess("{\"result\":{\"id\":7}}", MediaType.APPLICATION_JSON));
        bitrix.expect(requestTo(PORTAL + "biconnector.table.list"))
                .andExpect(jsonPath("$.filter.sourceId").value(7))
                .andRespond(withSuccess("{\"result\":[]}", MediaType.APPLICATION_JSON));
        bitrix.expect(requestTo(PORTAL + "biconnector.table.add"))
                .andExpect(jsonPath("$.fields.sourceId").value(7))
                .andExpect(jsonPath("$.fields.name").value("mdserp_pedidos_lineas"))
                .andExpect(jsonPath("$.fields.externalCode").value("pedidos_lineas"))
                .andExpect(jsonPath("$.fields.fields.length()").value(34))
                .andExpect(jsonPath("$.fields.fields[0].name").value("ID_LINEA"))
                .andExpect(jsonPath("$.fields.fields[0].externalCode").value("id_linea"))
                .andExpect(jsonPath("$.fields.fields[0].type").value("int"))
                .andRespond(withSuccess("{\"result\":{\"id\":10}}", MediaType.APPLICATION_JSON));
        bitrix.expect(requestTo(PORTAL + "biconnector.table.add"))
                .andExpect(jsonPath("$.fields.name").value("mdserp_pedidos_notas"))
                .andRespond(withSuccess("{\"result\":{\"id\":11}}", MediaType.APPLICATION_JSON));

        Result result = service("local.abc").register(TOKEN);

        bitrix.verify();
        assertThat(result).isEqualTo(new Result(4, 7, List.of("pedidos_lineas", "pedidos_notas"), List.of()));
    }

    @Test
    void reinstalacionActualizaSinDuplicar() {
        expect("app.info", "{\"result\":{\"ID\":1,\"CODE\":\"local.abc\"}}");
        expect("biconnector.connector.list", "{\"result\":[{\"id\":2,\"title\":\"Otro\"},{\"id\":4,\"title\":\"MDSERP Pedidos\"}]}");
        bitrix.expect(requestTo(PORTAL + "biconnector.connector.update"))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.fields.urlData").value("https://erp.test/api/bi-connector/data"))
                .andRespond(withSuccess("{\"result\":true}", MediaType.APPLICATION_JSON));
        expect("biconnector.source.list", "{\"result\":[{\"id\":7,\"connectorId\":4}]}");
        bitrix.expect(requestTo(PORTAL + "biconnector.source.update"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.fields.settings.key").value(KEY))
                .andRespond(withSuccess("{\"result\":true}", MediaType.APPLICATION_JSON));
        expect("biconnector.table.list",
                "{\"result\":[{\"id\":10,\"externalCode\":\"pedidos_lineas\"},{\"id\":11,\"externalCode\":\"pedidos_notas\"}]}");

        Result result = service("").register(TOKEN);

        bitrix.verify();
        assertThat(result).isEqualTo(new Result(4, 7, List.of(), List.of("pedidos_lineas", "pedidos_notas")));
    }

    @Test
    void rechazaTokenInvalidoSinRegistrarNada() {
        bitrix.expect(requestTo(PORTAL + "app.info"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"invalid_token\",\"error_description\":\"The access token provided is invalid.\"}"));

        assertThatThrownBy(() -> service("").register(TOKEN)).isInstanceOf(InvalidAppTokenException.class);
        bitrix.verify();
    }

    @Test
    void rechazaTokenDeOtraAplicacion() {
        expect("app.info", "{\"result\":{\"ID\":9,\"CODE\":\"local.otra\"}}");

        assertThatThrownBy(() -> service("local.abc").register(TOKEN)).isInstanceOf(InvalidAppTokenException.class);
        bitrix.verify();
    }

    // Bitrix24 devuelve los errores de biconnector con HTTP 200 dentro de "result"
    @Test
    void detectaErrorDentroDeResult() {
        expect("app.info", "{\"result\":{\"ID\":1}}");
        expect("biconnector.connector.list", "{\"result\":[]}");
        expect("biconnector.connector.add",
                "{\"result\":{\"error\":\"ACCESS_DENIED\",\"error_description\":\"Access denied\"}}");

        assertThatThrownBy(() -> service("").register(TOKEN))
                .isInstanceOf(BitrixApiException.class)
                .hasMessageContaining("ACCESS_DENIED");
    }

    @Test
    void sinClaveConfiguradaNoLlamaABitrix() {
        BiConnectorRegistrationService sinClave = new BiConnectorRegistrationService(client, tables, new ObjectMapper(),
                "https://erp.test/api", "", "");

        assertThatThrownBy(() -> sinClave.register(TOKEN)).isInstanceOf(IllegalStateException.class);
        bitrix.verify();
    }

    // Reglas de table.add: tabla en minusculas; columnas A-Z, 0-9 y _ con maximo 32 caracteres
    @Test
    void nombresDeTablasYColumnasCumplenLasReglasDeBitrix() {
        for (BiTable<?> table : tables.all()) {
            assertThat(BiConnectorRegistrationService.TABLE_PREFIX + table.code()).matches("[a-z][a-z0-9_]*");
            table.columns().forEach(column ->
                    assertThat(column.code().toUpperCase()).matches("[A-Z][A-Z0-9_]{0,31}"));
        }
    }
}
