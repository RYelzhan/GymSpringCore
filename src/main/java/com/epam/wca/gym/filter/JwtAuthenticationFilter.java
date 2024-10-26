package com.epam.wca.gym.filter;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.authentication.JwtAuthException;
import com.epam.wca.gym.service.impl.JwtService;
import com.epam.wca.gym.service.impl.LoginAttemptService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private Set<String> allowedPrefixes;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LoginAttemptService loginAttemptService;

    @Value("${allowed.prefixes}")
    public void setAllowedPrefixes(String[] prefixes) {
        allowedPrefixes = Arrays.stream(prefixes).collect(Collectors.toSet());
    }

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

        if (loginAttemptService.isBlocked()) {
            response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            response.getWriter().write("Too much Authentication Attempts. Wait 15 minutes.");
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        try {
            validateBearerTokenIsPresent(authHeader);
        } catch (com.epam.wca.gym.exception.AuthenticationException e) {
            respondWithUnauthorized(
                    response,
                    new JwtAuthException(e.getMessage(), e)
            );
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
        } catch (ExpiredJwtException e) {
            respondWithUnauthorized(
                    response,
                    new JwtAuthException("Token is Expired. Refresh it!", e)
            );
        } catch (SignatureException | MalformedJwtException e) {
            respondWithUnauthorized(
                    response,
                    new JwtAuthException("Token is invalid.", e)
            );
        }
    }

    private boolean isUriAllowed(Optional<String> uri) {
        return uri.isPresent() && allowedPrefixes.stream().anyMatch(uri.get()::startsWith);
    }

    private void validateBearerTokenIsPresent(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new com.epam.wca.gym.exception.AuthenticationException("Missing or malformed Bearer JWT token.");
        }
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
        try {
            User user = (User) userDetailsService.loadUserByUsername(username);

            jwtService.validateToken(jwtToken, user);
            setAuthenticationForUser(request, user);
            filterChain.doFilter(request, response);
        } catch (com.epam.wca.gym.exception.AuthenticationException | UsernameNotFoundException e) {
            respondWithUnauthorized(
                    response,
                    new JwtAuthException("Token is invalid.", e)
            );
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

    private void respondWithUnauthorized(HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Error: %s".formatted(e.getMessage()));


        applicationEventPublisher.publishEvent(
                new AuthenticationFailureBadCredentialsEvent(
                        new UsernamePasswordAuthenticationToken(null, null, null),
                        e)
        );
    }
}
