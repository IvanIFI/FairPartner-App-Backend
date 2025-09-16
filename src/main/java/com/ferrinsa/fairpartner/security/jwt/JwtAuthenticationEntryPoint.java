package com.ferrinsa.fairpartner.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");

        //FIXME: una vez en produccion quitar este log de la consola, ¡¡solo para debug!!
        log.warn("Intento de acceso no autorizado. IP=[{}], Método=[{}], Path=[{}], AuthHeader=[{}], Causa=[{}]",
                ip, method, path, authHeader != null ? "Presente" : "No presente", authException.getMessage());
        //FIXME: //////////////////////////////////////////////////////////////////////////

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Acceso denegado");
        problemDetail.setDetail("Credenciales inválidas");
        problemDetail.setProperty("path", path);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        mapper.writeValue(response.getOutputStream(), problemDetail);
    }

}
