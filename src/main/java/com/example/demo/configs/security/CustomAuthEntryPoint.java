package com.example.demo.configs.security;

import com.example.demo.utils.messages.AuthMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        String redirect_url = UriComponentsBuilder.fromPath("/login")
            .queryParam("error", URLEncoder.encode(AuthMessage.UNAUTHENTICATED_USER, "UTF-8")).build().toUriString();

        response.sendRedirect(redirect_url);
    }
}
