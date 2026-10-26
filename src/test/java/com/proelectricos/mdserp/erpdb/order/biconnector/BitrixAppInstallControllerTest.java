package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.InvalidAppTokenException;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BitrixAppInstallControllerTest {

    private BiConnectorRegistrationService registrationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        registrationService = mock(BiConnectorRegistrationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new BitrixAppInstallController(registrationService, new ObjectMapper())).build();
    }

    @Test
    void registraElConectorConElTokenDeLaInstalacion() throws Exception {
        when(registrationService.register("tok")).thenReturn(new Result(4, 7, List.of("pedidos_lineas"), List.of("pedidos_notas"), Map.of()));

        mockMvc.perform(post("/bitrix-app/install").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("auth[access_token]", "tok")
                        .param("auth[refresh_token]", "ref")
                        .param("auth[domain]", "proelectricos.bitrix24.es"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.connectorId").value(4))
                .andExpect(jsonPath("$.sourceId").value(7))
                .andExpect(jsonPath("$.createdTables[0]").value("pedidos_lineas"));
    }

    @Test
    void aceptaTambienCuerpoJson() throws Exception {
        when(registrationService.register("tok")).thenReturn(new Result(4, 7, List.of(), List.of(), Map.of()));

        mockMvc.perform(post("/bitrix-app/install").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"auth\":{\"access_token\":\"tok\"}}"))
                .andExpect(status().isOk());
    }

    // Al reinstalar o abrir la app, Bitrix24 carga la URL en el iframe con el token en AUTH_ID
    @Test
    void aceptaElTokenDelIframeEnAuthId() throws Exception {
        when(registrationService.register("tok")).thenReturn(
                new Result(4, 7, List.of(), List.of("pedidos_lineas"), Map.of("pedidos_lineas", List.of("sede_zona"))));

        mockMvc.perform(post("/bitrix-app/install").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("DOMAIN", "proelectricos.bitrix24.es")
                        .param("AUTH_ID", "tok")
                        .param("REFRESH_ID", "ref")
                        .param("PLACEMENT", "DEFAULT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addedColumns.pedidos_lineas[0]").value("sede_zona"));
    }

    @Test
    void sinTokenResponde400() throws Exception {
        mockMvc.perform(post("/bitrix-app/install").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("auth[refresh_token]", "ref"))
                .andExpect(status().isBadRequest());
        verify(registrationService, never()).register(any());
    }

    @Test
    void tokenInvalidoResponde401() throws Exception {
        when(registrationService.register("falso")).thenThrow(new InvalidAppTokenException("Token rechazado"));

        mockMvc.perform(post("/bitrix-app/install").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("auth[access_token]", "falso"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_TOKEN"));
    }
}
