package com.proelectricos.mdserp.chatbot.web;

import com.proelectricos.mdserp.chatbot.service.ChatbotService;
import com.proelectricos.mdserp.config.security.SecurityConfig;
import com.proelectricos.mdserp.config.security.ToolUserAccessDeniedHandler;
import com.proelectricos.mdserp.config.security.ToolUserAuthenticationEntryPointHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Seguridad y validación del chatbot web, sin llamar a Gemini (ChatbotService simulado).
 */
@WebMvcTest(ChatbotController.class)
@Import({SecurityConfig.class, ToolUserAccessDeniedHandler.class, ToolUserAuthenticationEntryPointHandler.class})
@TestPropertySource(properties = "chatbot.enabled=true")
class ChatbotControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ChatbotService chatbotService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void respondeEnLaConversacionDelUsuario() throws Exception {
        when(chatbotService.responder("web:usuario-1", "¿Cuántos pedidos hay?")).thenReturn("Hay 10 pedidos.");

        mvc.perform(post("/api/chatbot/mensaje")
                        .with(jwt().jwt(j -> j.subject("usuario-1")).authorities(new SimpleGrantedAuthority("ROLE_CHATBOT")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\":\"  ¿Cuántos pedidos hay?  \"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respuesta").value("Hay 10 pedidos."));
    }

    @Test
    void rechazaUsuariosSinRolChatbot() throws Exception {
        mvc.perform(post("/api/chatbot/mensaje")
                        .with(jwt().jwt(j -> j.subject("usuario-1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\":\"hola\"}"))
                .andExpect(status().isForbidden());
        verify(chatbotService, never()).responder(anyString(), anyString());
    }

    @Test
    void rechazaMensajesVacios() throws Exception {
        mvc.perform(post("/api/chatbot/mensaje")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CHATBOT")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\":\"   \"}"))
                .andExpect(status().isBadRequest());
        verify(chatbotService, never()).responder(anyString(), anyString());
    }

    @Test
    void reiniciaSoloLaConversacionDelUsuario() throws Exception {
        mvc.perform(post("/api/chatbot/reiniciar")
                        .with(jwt().jwt(j -> j.subject("usuario-1")).authorities(new SimpleGrantedAuthority("ROLE_CHATBOT"))))
                .andExpect(status().isNoContent());
        verify(chatbotService).reiniciar("web:usuario-1");
    }
}
