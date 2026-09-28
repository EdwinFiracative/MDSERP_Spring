package com.proelectricos.mdserp.service.bitrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Servicio encargado de comunicarse con la API REST de Bitrix24 (módulo CRM).
 */
@Slf4j
@Service
public class BitrixService {

    private final RestClient bitrixRestClient;

    public BitrixService(@Qualifier("bitrixRestClient") RestClient bitrixRestClient) {
        this.bitrixRestClient = bitrixRestClient;
    }

    /**
     * Actualiza campos de una negociación (deal) en Bitrix24 usando el método crm.deal.update.
     *
     * Petición que se envía:
     *   POST {webhook}/crm.deal.update.json
     *   {
     *     "id": 123,
     *     "fields": { "UF_CRM_1790282776": 11778708 }
     *   }
     *
     * Bitrix responde {"result": true, ...} si la actualización fue exitosa, o
     * {"error": "...", "error_description": "..."} si algo falló.
     *
     * @param dealId ID de la negociación en Bitrix24
     * @param fields mapa campo -> valor con los campos a modificar
     * @return la respuesta completa de Bitrix (útil para depurar)
     */
    public Map<String, Object> updateDeal(Long dealId, Map<String, Object> fields) {
        Map<String, Object> body = Map.of(
                "id", dealId,
                "fields", fields
        );

        log.info("Bitrix crm.deal.update -> dealId={}, fields={}", dealId, fields);

        Map<String, Object> response;
        try {
            response = bitrixRestClient.post()
                    .uri("crm.deal.update.json")
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (RestClientException e) {
            // Errores de red o respuestas HTTP 4xx/5xx de Bitrix (p. ej. deal inexistente, token inválido)
            log.error("Error llamando crm.deal.update en Bitrix para dealId={}", dealId, e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Error al actualizar el deal " + dealId + " en Bitrix24: " + e.getMessage(), e);
        }

        // Bitrix puede responder 200 pero sin "result": true; se valida explícitamente
        if (response == null || !Boolean.TRUE.equals(response.get("result"))) {
            log.error("Bitrix no confirmó la actualización del deal {}: {}", dealId, response);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Bitrix24 no confirmó la actualización del deal " + dealId + ": " + response);
        }

        log.info("Bitrix crm.deal.update OK -> dealId={}", dealId);
        return response;
    }
}
