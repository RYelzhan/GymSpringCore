package com.epam.wca.gym.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
public class LoggerFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        System.out.println("Request Method: {}" + method);
        System.out.println("Request URI: {}" + requestURI);

        var wrappedResponse =
                new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        System.out.println("Continue Filtering");
        chain.doFilter(servletRequest, wrappedResponse);

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
