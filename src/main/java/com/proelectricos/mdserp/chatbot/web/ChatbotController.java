package com.proelectricos.mdserp.chatbot.web;

import com.proelectricos.mdserp.chatbot.service.ChatbotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * El mismo chatbot de Telegram (mismas instrucciones y herramientas sobre ErpDb) expuesto para el front.
 * Cada usuario de Keycloak tiene su propia conversación. Como el bot puede consultar toda ErpDb,
 * se exige el rol "chatbot" (equivalente a la lista de chats autorizados de Telegram).
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/chatbot")
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
@PreAuthorize("hasAuthority('ROLE_CHATBOT')")
public class ChatbotController {

    private static final int MAX_CARACTERES = 4000;

    private final ChatbotService chatbotService;

    public record MensajeRequest(String texto) {
    }

    public record MensajeResponse(String respuesta) {
    }

    @PostMapping("/mensaje")
    public MensajeResponse enviarMensaje(@AuthenticationPrincipal Jwt jwt, @RequestBody MensajeRequest mensaje) {
        String texto = mensaje.texto() == null ? "" : mensaje.texto().trim();
        if (texto.isEmpty() || texto.length() > MAX_CARACTERES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El mensaje debe tener entre 1 y " + MAX_CARACTERES + " caracteres.");
        }
        try {
            return new MensajeResponse(chatbotService.responder(conversacion(jwt), texto));
        } catch (RuntimeException e) {
            log.error("Chatbot web: error procesando el mensaje", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Hubo un error al procesar tu solicitud.", e);
        }
    }

    @PostMapping("/reiniciar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reiniciar(@AuthenticationPrincipal Jwt jwt) {
        chatbotService.reiniciar(conversacion(jwt));
    }

    private static String conversacion(Jwt jwt) {
        return "web:" + jwt.getSubject();
    }
}
