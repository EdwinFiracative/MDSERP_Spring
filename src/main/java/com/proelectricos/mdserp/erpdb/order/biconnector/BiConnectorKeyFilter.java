package com.proelectricos.mdserp.erpdb.order.biconnector;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Las rutas /bi-connector/** no llevan JWT de Keycloak (Bitrix24 no lo envia): se protegen con la clave
 * compartida que el conector manda en cada llamada como "connection[key]" (setting "key" del conector).
 * Sin clave configurada en el backend se rechaza todo.
 */
@Slf4j
@Component
public class BiConnectorKeyFilter extends OncePerRequestFilter {

    public static final String PATH_PREFIX = "/bi-connector/";
    public static final String KEY_SETTING = "key";
    private static final String KEY_PARAM = "connection[" + KEY_SETTING + "]";

    private final byte[] expectedKey;

    public BiConnectorKeyFilter(@Value("${b24.connector-key:}") String connectorKey) {
        this.expectedKey = connectorKey.strip().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().substring(request.getContextPath().length()).startsWith(PATH_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String provided = request.getParameter(KEY_PARAM);
        if (expectedKey.length == 0 || provided == null
                || !MessageDigest.isEqual(expectedKey, provided.strip().getBytes(StandardCharsets.UTF_8))) {
            log.warn("Conector BI: clave invalida o ausente en {} desde {}", request.getRequestURI(), request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("{\"error\":\"INVALID_KEY\",\"error_description\":\"Clave del conector invalida\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
