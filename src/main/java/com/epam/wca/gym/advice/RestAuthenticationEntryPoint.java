package com.epam.wca.gym.advice;

import com.epam.wca.gym.exception.UserBlockedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        String errorMessage;

        if (authException.getCause() instanceof UserBlockedException ||
                authException.getCause() instanceof UsernameNotFoundException ||
                authException.getCause() instanceof AccessDeniedException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        errorMessage = authException.getCause().getMessage();

        response.getWriter().write("Error: %s".formatted(errorMessage));
    }
}

