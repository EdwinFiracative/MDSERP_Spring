package com.proelectricos.mdserp.chatbot.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Selección del token de Telegram por perfil (secciones #--- de application.properties)
 * y error claro al arrancar cuando falta el token del perfil activo.
 */
class TelegramTokenPorPerfilTest {

    /** Contexto vacío: solo se cargan application.properties y .env, sin beans ni base de datos. */
    @Configuration(proxyBeanMethods = false)
    static class Vacio {
    }

    // Los argumentos de línea de comandos tienen prioridad sobre .env, así el test no depende de los valores reales
    private String tokenResuelto(String... args) {
        try (ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Vacio.class)
                .web(WebApplicationType.NONE)
                .run(args)) {
            return ctx.getEnvironment().getProperty("chatbot.telegram.token");
        }
    }

    @Test
    void perfilPorDefectoEsDevYUsaElTokenDeDesarrollo() {
        assertThat(tokenResuelto("--TELEGRAM_BOT_TOKEN_DEV=token-dev", "--TELEGRAM_BOT_TOKEN_PROD=token-prod"))
                .isEqualTo("token-dev");
    }

    @Test
    void perfilProdUsaElTokenDeProduccion() {
        assertThat(tokenResuelto("--spring.profiles.active=prod",
                "--TELEGRAM_BOT_TOKEN_DEV=token-dev", "--TELEGRAM_BOT_TOKEN_PROD=token-prod"))
                .isEqualTo("token-prod");
    }

    @Test
    void devuelveElTokenCuandoExiste() {
        assertThat(new ChatbotProperties.Telegram("abc", Set.of()).exigirToken("dev")).isEqualTo("abc");
    }

    @Test
    void sinTokenEnProdIndicaLaVariableDeProduccion() {
        assertThatThrownBy(() -> new ChatbotProperties.Telegram("", Set.of()).exigirToken("prod"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("TELEGRAM_BOT_TOKEN_PROD")
                .hasMessageContaining("CHATBOT_ENABLED=false");
    }

    @Test
    void sinTokenEnDevIndicaLaVariableDeDesarrollo() {
        assertThatThrownBy(() -> new ChatbotProperties.Telegram(null, Set.of()).exigirToken("dev"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("TELEGRAM_BOT_TOKEN_DEV");
    }

    @Test
    void sinPerfilDevNiProdLoIndica() {
        assertThatThrownBy(() -> new ChatbotProperties.Telegram(" ", Set.of()).exigirToken("test"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Active el perfil dev o prod");
    }
}
