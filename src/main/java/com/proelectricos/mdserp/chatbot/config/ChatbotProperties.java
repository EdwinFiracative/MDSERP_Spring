package com.proelectricos.mdserp.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Configuración del chatbot (prefijo "chatbot" en application.properties).
 * Los secretos se leen de variables de entorno o del archivo .env (ver application.properties).
 */
@ConfigurationProperties(prefix = "chatbot")
public record ChatbotProperties(
        boolean enabled,
        Gemini gemini,
        Telegram telegram,
        Database database,
        Bitrix bitrix) {

    /**
     * @param maxToolCalls       rondas máximas de llamadas a herramientas por mensaje del usuario
     * @param thinkingLevel      razonamiento del modelo: minimal, low, medium o high (vacío = el del modelo).
     *                           Menos razonamiento = respuestas más rápidas
     * @param minutosInactividad tras este tiempo sin mensajes la conversación empieza de cero (0 = nunca),
     *                           para no reenviar a Gemini un historial cada vez más largo
     */
    public record Gemini(String apiKey, String model, int maxToolCalls, String thinkingLevel, int minutosInactividad) {
    }

    /**
     * @param allowedChatIds chats de Telegram autorizados; si está vacío nadie puede usar el bot
     */
    public record Telegram(String token, Set<Long> allowedChatIds) {

        public boolean autorizado(long chatId) {
            return allowedChatIds != null && allowedChatIds.contains(chatId);
        }

        /**
         * Devuelve el token o lanza un error claro indicando qué variable falta según el perfil activo
         * (dev -> TELEGRAM_BOT_TOKEN_DEV, prod -> TELEGRAM_BOT_TOKEN_PROD).
         *
         * @param perfilesActivos perfiles de Spring activos (Environment#getActiveProfiles)
         */
        public String exigirToken(String... perfilesActivos) {
            if (token != null && !token.isBlank()) {
                return token;
            }
            List<String> perfiles = perfilesActivos == null ? List.of() : Arrays.asList(perfilesActivos);
            String variable;
            if (perfiles.contains("prod")) {
                variable = "TELEGRAM_BOT_TOKEN_PROD";
            } else if (perfiles.contains("dev")) {
                variable = "TELEGRAM_BOT_TOKEN_DEV";
            } else {
                throw new IllegalStateException("chatbot.enabled=true pero ningún perfil activo (" + perfiles
                        + ") define chatbot.telegram.token. Active el perfil dev o prod (SPRING_PROFILES_ACTIVE)"
                        + " o desactive el chatbot con CHATBOT_ENABLED=false.");
            }
            throw new IllegalStateException("chatbot.enabled=true pero falta el token del bot de Telegram para el"
                    + " perfil " + perfiles + ": defina " + variable + " en .env o como variable de entorno,"
                    + " o desactive el chatbot con CHATBOT_ENABLED=false.");
        }
    }

    /**
     * Conexión exclusiva del chatbot, apuntando a ErpDb.
     * Lo ideal es un usuario SQL con solo db_datareader sobre ErpDb.
     */
    public record Database(String url, String username, String password, int maxRows, int queryTimeoutSeconds) {
    }

    /**
     * Webhook entrante de Bitrix24 que usa el chatbot para leer negocios (solo métodos de lectura crm.*).
     * Es distinto de bitrix.webhook-url, que el backend usa para actualizar negocios.
     */
    public record Bitrix(String webhookUrl) {
    }
}
