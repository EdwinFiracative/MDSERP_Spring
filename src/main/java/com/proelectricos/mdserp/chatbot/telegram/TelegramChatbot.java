package com.proelectricos.mdserp.chatbot.telegram;

import com.google.genai.types.Part;
import com.proelectricos.mdserp.chatbot.config.ChatbotProperties;
import com.proelectricos.mdserp.chatbot.service.ChatbotService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bot de Telegram (long polling): recibe texto o notas de voz, las pasa a {@link ChatbotService}
 * y responde en el mismo chat. Solo atiende los chats configurados en chatbot.telegram.allowed-chat-ids.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class TelegramChatbot implements LongPollingUpdateConsumer {

    private static final int MAX_CARACTERES_MENSAJE = 3500;
    private static final String INSTRUCCION_AUDIO =
            "El usuario envió esta nota de voz. Interpreta lo que dice y respóndele como si lo hubiera escrito.";

    private final TelegramClient telegram;
    private final TelegramBotsLongPollingApplication botsApplication;
    private final ChatbotService chatbot;
    private final ChatbotProperties.Telegram config;
    // Las respuestas de Gemini tardan; cada mensaje se atiende en su propio hilo para no bloquear a los demás chats
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public TelegramChatbot(TelegramClient telegram, TelegramBotsLongPollingApplication botsApplication,
                           ChatbotService chatbot, ChatbotProperties properties) {
        this.telegram = telegram;
        this.botsApplication = botsApplication;
        this.chatbot = chatbot;
        this.config = properties.telegram();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registrar() {
        try {
            botsApplication.registerBot(config.token(), this);
            log.info("Chatbot de Telegram en ejecución. Chats autorizados: {}", config.allowedChatIds());
        } catch (TelegramApiException e) {
            log.error("No se pudo iniciar el bot de Telegram", e);
        }
    }

    @PreDestroy
    public void detener() {
        executor.shutdownNow();
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(update -> executor.execute(() -> {
            try {
                atender(update);
            } catch (Exception e) {
                log.error("Error atendiendo el mensaje de Telegram", e);
            }
        }));
    }

    private void atender(Update update) throws TelegramApiException, IOException {
        Message mensaje = update.getMessage();
        if (mensaje == null) {
            return;
        }
        long chatId = mensaje.getChatId();
        if (!config.autorizado(chatId)) {
            log.warn("Chatbot: mensaje de un chat no autorizado {}", chatId);
            enviar(chatId, "⛔ No tienes acceso a este bot. Comparte este ID con el administrador: " + chatId);
            return;
        }
        if (mensaje.hasText()) {
            String texto = mensaje.getText().trim();
            if (texto.startsWith("/start")) {
                enviar(chatId, "Hola, pregúntame lo que quieras sobre la base de datos ErpDb. "
                        + "/reset reinicia la conversación.");
            } else if (texto.startsWith("/reset")) {
                chatbot.reiniciar(chatId);
                enviar(chatId, "Conversación reiniciada.");
            } else {
                procesar(chatId, List.of(Part.fromText(texto)));
            }
        } else if (mensaje.hasVoice() || mensaje.hasAudio()) {
            // Gemini interpreta el audio directamente, sin transcribirlo aparte
            String fileId = mensaje.hasVoice() ? mensaje.getVoice().getFileId() : mensaje.getAudio().getFileId();
            String mimeType = mensaje.hasVoice() ? mensaje.getVoice().getMimeType() : mensaje.getAudio().getMimeType();
            File archivo = telegram.execute(new GetFile(fileId));
            byte[] audio;
            try (InputStream in = telegram.downloadFileAsStream(archivo)) {
                audio = in.readAllBytes();
            }
            procesar(chatId, List.of(
                    Part.fromBytes(audio, mimeType == null ? "audio/ogg" : mimeType),
                    Part.fromText(INSTRUCCION_AUDIO)));
        }
    }

    private void procesar(long chatId, List<Part> partes) throws TelegramApiException {
        Message espera = telegram.execute(SendMessage.builder()
                .chatId(chatId)
                .text("🔍 Analizando y consultando la base de datos...")
                .build());
        String respuesta;
        try {
            respuesta = chatbot.responder(chatId, partes);
        } catch (RuntimeException e) {
            log.error("Chatbot: error procesando el mensaje", e);
            respuesta = "Hubo un error al procesar tu solicitud: " + e.getMessage();
        }
        List<String> partesRespuesta = TelegramFormatter.partir(respuesta, MAX_CARACTERES_MENSAJE);
        editar(chatId, espera.getMessageId(), partesRespuesta.get(0));
        for (String parte : partesRespuesta.subList(1, partesRespuesta.size())) {
            enviar(chatId, parte);
        }
    }

    private void enviar(long chatId, String markdown) throws TelegramApiException {
        try {
            telegram.execute(SendMessage.builder().chatId(chatId)
                    .text(TelegramFormatter.html(markdown)).parseMode(ParseMode.HTML).build());
        } catch (TelegramApiException e) {
            // Si Telegram rechaza el HTML se envía el texto plano
            telegram.execute(SendMessage.builder().chatId(chatId).text(markdown).build());
        }
    }

    private void editar(long chatId, Integer messageId, String markdown) throws TelegramApiException {
        try {
            telegram.execute(EditMessageText.builder().chatId(chatId).messageId(messageId)
                    .text(TelegramFormatter.html(markdown)).parseMode(ParseMode.HTML).build());
        } catch (TelegramApiException e) {
            telegram.execute(EditMessageText.builder().chatId(chatId).messageId(messageId).text(markdown).build());
        }
    }
}
