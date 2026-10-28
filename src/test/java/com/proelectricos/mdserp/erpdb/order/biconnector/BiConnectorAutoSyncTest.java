package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.BitrixApiException;
import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BiConnectorAutoSyncTest {

    private BitrixAppTokenService tokenService;
    private BiConnectorRegistrationService registrationService;
    private BiConnectorAutoSync autoSync;

    @BeforeEach
    void setUp() {
        tokenService = mock(BitrixAppTokenService.class);
        registrationService = mock(BiConnectorRegistrationService.class);
        autoSync = new BiConnectorAutoSync(tokenService, registrationService);
    }

    @Test
    void registraConElTokenRenovado() {
        when(tokenService.isConfigured()).thenReturn(true);
        when(tokenService.refreshAccessToken()).thenReturn(Optional.of("acc"));
        when(registrationService.register("acc")).thenReturn(new Result(4, 7, List.of(), List.of("pedidos_lineas"),
                Map.of("pedidos_lineas", List.of("vendedor_cedula"))));

        autoSync.sync();

        verify(registrationService).register("acc");
    }

    @Test
    void sinTokenNoRegistra() {
        when(tokenService.isConfigured()).thenReturn(true);
        when(tokenService.refreshAccessToken()).thenReturn(Optional.empty());

        autoSync.sync();

        verify(registrationService, never()).register(any());
    }

    @Test
    void sinConfiguracionNoHaceNada() {
        autoSync.sync();

        verify(tokenService, never()).refreshAccessToken();
        verify(registrationService, never()).register(any());
    }

    // Un error de Bitrix24 no debe tumbar el arranque
    @Test
    void unErrorDeBitrixNoSePropaga() {
        when(tokenService.isConfigured()).thenReturn(true);
        when(tokenService.refreshAccessToken()).thenReturn(Optional.of("acc"));
        when(registrationService.register("acc")).thenThrow(new BitrixApiException("biconnector.table.get", "ERR", "x"));

        autoSync.sync();
    }
}
