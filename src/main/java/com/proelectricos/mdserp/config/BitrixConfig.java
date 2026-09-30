package com.proelectricos.mdserp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * Configuración del cliente HTTP para consumir la API REST de Bitrix24.
 *
 * Bitrix24 expone sus métodos (crm.deal.update, crm.deal.get, etc.) a través de un
 * "webhook entrante" con el formato:
 *   https://{portal}.bitrix24.com/rest/{userId}/{token}/
 *
 * Esa URL se lee de la propiedad "bitrix.webhook-url" (application.properties), que a su vez
 * toma el valor de la variable de entorno BITRIX_WEBHOOK_URL para no dejar el token en el código.
 */
@Configuration
public class BitrixConfig {

    /**
     * RestClient (cliente HTTP síncrono de Spring 6.1+) con la URL base del webhook ya configurada.
     * Así, al llamar un método solo se indica su nombre, p. ej. "crm.deal.update.json".
     */
    @Bean
    public RestClient bitrixRestClient(@Value("${bitrix.webhook-url}") String webhookUrl) {
        // Se asegura que la URL termine en "/" para que al concatenar el método quede bien formada
        String baseUrl = webhookUrl.endsWith("/") ? webhookUrl : webhookUrl + "/";
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
