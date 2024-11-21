package com.epam.wca.gym.filter;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.AuthenticationException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * @deprecated Deprecated since transfer to spring security.
 * Custom filter used for basic authentication checks.
 * Enabling pass of request is through basic authentication header or being in allowed prefixes.
 */

@Deprecated(since = "2.2")
@Component
@RequiredArgsConstructor
@Profile("secure")
public class AuthenticationFilter extends HttpFilter {
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;
    private static final Set<String> ALLOWED_PREFIXES = Set.of(
            "/authenticate",
            "/h2-console",
            "/admin"
    );

    private final transient AuthService authService;
    private final transient RequestCounterMetrics requestCounterMetrics;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        requestCounterMetrics.increaseCounter();

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        Optional<String> uri = Optional.ofNullable(httpRequest.getRequestURI());

        // excluding authentication URI from checking
        if (uri.isPresent() && ALLOWED_PREFIXES.stream().anyMatch(uri.get()::startsWith)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");

        try {
            User user = authService.authenticate(authHeader);

            // Store user in the request attributes
            httpRequest.setAttribute(authenticatedUserRequestAttributeName, user);

            // Continue the request if authentication is successful

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException e) {
            // If authentication fails, respond with 401 Unauthorized
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("HTTP Status 401 - Unauthorized");
        }
    }
}
