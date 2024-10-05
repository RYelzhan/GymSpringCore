package com.epam.wca.gym.filter;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.metrics.RequestCounterMetrics;
import com.epam.wca.gym.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("secure")
public class AuthenticationFilter extends HttpFilter {
    private static final String AUTHENTICATION_URI = "/gym/authenticate";
    private final AuthService authService;
    private final RequestCounterMetrics requestCounterMetrics;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        requestCounterMetrics.increaseCounter();

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        Optional<String> uri = Optional.ofNullable(httpRequest.getRequestURI());

        // excluding authentication URI from checking
        if (uri.isPresent() && httpRequest.getRequestURI().startsWith(AUTHENTICATION_URI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");

        try {
            User user = authService.authenticate(authHeader);

            // Store user in the request attributes
            httpRequest.setAttribute("authenticatedUser", user);

            // Continue the request if authentication is successful

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IllegalArgumentException e){
            // If authentication fails, respond with 401 Unauthorized
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("HTTP Status 401 - Unauthorized");
        }
    }
}
