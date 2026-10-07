package com.proelectricos.mdserp.chatbot.service;

import com.google.genai.Chat;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.proelectricos.mdserp.chatbot.config.ChatbotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Conversación con Gemini: un historial independiente por chat y el ciclo de llamadas a herramientas
 * (el modelo pide una función, se ejecuta contra ErpDb y se le devuelve el resultado hasta que responde en texto).
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class ChatbotService {

    private static final String SIN_RESPUESTA = "No obtuve respuesta.";

    private final Client gemini;
    private final ErpDbChatTools tools;
    private final ChatbotProperties.Gemini config;
    private final String instrucciones;
    private final Map<Long, Chat> chats = new ConcurrentHashMap<>();

    public ChatbotService(Client gemini, ErpDbChatTools tools, ChatbotProperties properties) {
        this.gemini = gemini;
        this.tools = tools;
        this.config = properties.gemini();
        this.instrucciones = leerInstrucciones();
    }

    /** Envía un texto del usuario y devuelve la respuesta final del modelo. */
    public String responder(long chatId, String texto) {
        return responder(chatId, List.of(Part.fromText(texto)));
    }

    /** Envía partes arbitrarias (texto, audio...) y devuelve la respuesta final del modelo. */
    public String responder(long chatId, List<Part> partes) {
        Chat chat = chats.computeIfAbsent(chatId, id -> crearChat());
        // El historial de un Chat no es seguro entre hilos: un mensaje a la vez por conversación
        synchronized (chat) {
            try {
                GenerateContentResponse respuesta = chat.sendMessage(Content.builder().role("user").parts(partes).build());
                for (int ronda = 0; !respuesta.functionCalls().isEmpty(); ronda++) {
                    if (ronda >= config.maxToolCalls()) {
                        chats.remove(chatId, chat);
                        return "No pude completar la consulta con un número razonable de pasos. "
                                + "Intenta una pregunta más concreta.";
                    }
                    respuesta = chat.sendMessage(Content.builder()
                            .role("user")
                            .parts(respuestasDeFunciones(respuesta.functionCalls()))
                            .build());
                }
                String texto = respuesta.text();
                return texto == null || texto.isBlank() ? SIN_RESPUESTA : texto;
            } catch (RuntimeException e) {
                // Un historial con una llamada a función sin respuesta deja inservible la conversación
                chats.remove(chatId, chat);
                throw e;
            }
        }
    }

    public void reiniciar(long chatId) {
        chats.remove(chatId);
    }

    private List<Part> respuestasDeFunciones(List<FunctionCall> llamadas) {
        return llamadas.stream()
                .map(llamada -> {
                    FunctionResponse.Builder respuesta = FunctionResponse.builder()
                            .name(llamada.name().orElse(""))
                            .response(tools.ejecutar(llamada));
                    llamada.id().ifPresent(respuesta::id);
                    return Part.builder().functionResponse(respuesta.build()).build();
                })
                .toList();
    }

    private Chat crearChat() {
        String sistema = instrucciones + "\n\nFecha actual: " + LocalDate.now() + ".";
        return gemini.chats.create(config.model(), GenerateContentConfig.builder()
                .systemInstruction(Content.fromParts(Part.fromText(sistema)))
                .tools(tools.tool())
                .build());
    }

    private static String leerInstrucciones() {
        try {
            return new ClassPathResource("chatbot/instrucciones.md").getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer chatbot/instrucciones.md", e);
        }
    }
}
