package com.epam.wca.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
public class LoggerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse,
            FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        var wrappedResponse =
                new ContentCachingResponseWrapper(httpResponse);

        filterChain.doFilter(httpRequest, wrappedResponse);

        int status = wrappedResponse.getStatus();

        String responseBody = new String(
                wrappedResponse.getContentAsByteArray(),
                wrappedResponse.getCharacterEncoding()
        );

        log.info("Request Method: {}", method);
        log.info("Request URI: {}", requestURI);
        log.info("Response Status: {}", status);
        log.info("Response Body: {}", responseBody);

        wrappedResponse.copyBodyToResponse();
    }
}
