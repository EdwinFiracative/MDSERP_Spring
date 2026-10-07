package com.proelectricos.mdserp.chatbot.config;

import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import com.google.genai.types.HttpRetryOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Beans del chatbot. Solo se cargan con chatbot.enabled=true, para que la API arranque igual
 * sin las llaves de Gemini/Telegram (y para no tener dos instancias haciendo long polling con el mismo token).
 */
@Configuration
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(ChatbotProperties.class)
public class ChatbotConfig {

    @Bean
    public Client geminiClient(ChatbotProperties properties) {
        return Client.builder()
                .apiKey(properties.gemini().apiKey())
                // Reintenta errores temporales (429/503) de Gemini
                .httpOptions(HttpOptions.builder()
                        .retryOptions(HttpRetryOptions.builder().attempts(6).initialDelay(2.0))
                        .build())
                .build();
    }

    @Bean
    public TelegramClient telegramClient(ChatbotProperties properties) {
        return new OkHttpTelegramClient(properties.telegram().token());
    }

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsApplication() {
        return new TelegramBotsLongPollingApplication();
    }
}
