package com.proelectricos.mdserp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

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
     * Los timeouts evitan que un Bitrix24 lento o caído deje bloqueados los hilos del backend.
     */
    @Bean
    public RestClient bitrixRestClient(@Value("${bitrix.webhook-url}") String webhookUrl,
                                       @Value("${bitrix.connect-timeout:5s}") Duration connectTimeout,
                                       @Value("${bitrix.read-timeout:30s}") Duration readTimeout) {
        return jsonClient(webhookUrl, connectTimeout, readTimeout);
    }

    /**
     * RestClient hacia la API REST del portal con autenticacion OAuth de la aplicacion local
     * (https://{portal}/rest/{metodo} con "auth" en el cuerpo). Lo usa el registro del conector BI,
     * cuyos metodos no admiten webhook.
     */
    @Bean
    public RestClient bitrixAppRestClient(@Value("${b24.portal-url}") String portalUrl,
                                          @Value("${bitrix.connect-timeout:5s}") Duration connectTimeout,
                                          @Value("${bitrix.read-timeout:30s}") Duration readTimeout) {
        return jsonClient((portalUrl.endsWith("/") ? portalUrl : portalUrl + "/") + "rest/", connectTimeout, readTimeout);
    }

    private static RestClient jsonClient(String url, Duration connectTimeout, Duration readTimeout) {
        // Se asegura que la URL termine en "/" para que al concatenar el método quede bien formada
        String baseUrl = url.endsWith("/") ? url : url + "/";
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
