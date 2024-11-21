package com.epam.wca.gym.advice;

import com.epam.wca.gym.exception.authentication.UserBlockedAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String AUTHENTICATION_ERROR_MESSAGE = "Error: Authentication Failed.";
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        String errorMessage = AUTHENTICATION_ERROR_MESSAGE;

        if (authException instanceof UserBlockedAuthException) {
            errorMessage = authException.getMessage();
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);
    }
}

