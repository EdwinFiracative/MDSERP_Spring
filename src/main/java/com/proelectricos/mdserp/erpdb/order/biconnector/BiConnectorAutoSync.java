package com.proelectricos.mdserp.erpdb.order.biconnector;

import com.proelectricos.mdserp.erpdb.order.biconnector.BiConnectorRegistrationService.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Vuelve a registrar el conector BI sin que nadie abra la app en Bitrix24: al arrancar (cada despliegue)
 * y una vez al dia. Asi las columnas nuevas de {@link BiConnectorTables} llegan solas a BI Builder, y la
 * renovacion diaria mantiene vivo el refresh_token guardado.
 */
@Slf4j
@AllArgsConstructor
@Component
public class BiConnectorAutoSync {

    private final BitrixAppTokenService tokenService;
    private final BiConnectorRegistrationService registrationService;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        sync();
    }

    @Scheduled(cron = "${b24.connector-sync-cron:0 0 5 * * *}", zone = "America/Bogota")
    public void daily() {
        sync();
    }

    // Nunca lanza: un fallo de Bitrix24 no debe tumbar el arranque ni la tarea programada
    void sync() {
        if (!tokenService.isConfigured()) {
            log.info("Conector BI: sincronizacion automatica desactivada (faltan B24_APP_CLIENT_ID o B24_APP_CLIENT_SECRET)");
            return;
        }
        try {
            Optional<String> accessToken = tokenService.refreshAccessToken();
            if (accessToken.isEmpty()) {
                return;
            }
            Result result = registrationService.register(accessToken.get());
            log.info("Conector BI: sincronizacion automatica OK, columnas agregadas {}", result.addedColumns());
        } catch (RuntimeException e) {
            log.error("Conector BI: fallo la sincronizacion automatica: {}", e.getMessage());
        }
    }
}
