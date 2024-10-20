package com.epam.wca.gym.filter;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Set<String> ALLOWED_PREFIXES = Set.of(
            "/authentication",
            "/h2-console",
            "/admin",
            "/v3/api-docs"
    );

    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> uri = Optional.ofNullable(request.getRequestURI());

        if (isUriAllowed(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (!isBearerTokenPresent(authHeader)) {
            respondWithUnauthorized(response, "Jwt Token is required for Authentication.");
            return;
        }

        final String jwtToken = extractJwtToken(authHeader);

        try {
            String username = jwtService.extractUsername(jwtToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                handleAuthentication(request, response, filterChain, jwtToken, username);
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (UsernameNotFoundException e) {
            respondWithUnauthorized(response, e.getMessage());
        } catch (ExpiredJwtException e) {
            respondWithUnauthorized(response, "Token is Expired. Refresh it!");
        } catch (SignatureException e) {
            respondWithUnauthorized(response, "Token is invalid.");
        }
    }

    private boolean isUriAllowed(Optional<String> uri) {
        return uri.isPresent() && ALLOWED_PREFIXES.stream().anyMatch(uri.get()::startsWith);
    }

    private boolean isBearerTokenPresent(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String extractJwtToken(String authHeader) {
        return authHeader.substring(7); // Remove "Bearer " prefix
    }

    private void handleAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            String jwtToken,
            String username
    ) throws IOException, ServletException {
        User user = (User) userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwtToken, user)) {
            setAuthenticationForUser(request, user);
            filterChain.doFilter(request, response);
        } else {
            respondWithUnauthorized(response, "Token is invalid.");
        }
    }

    private void setAuthenticationForUser(HttpServletRequest request, User user) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        request.setAttribute(authenticatedUserRequestAttributeName, user);
    }

    private void respondWithUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Error: %s".formatted(message));
    }
}
