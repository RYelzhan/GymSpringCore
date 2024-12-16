package com.epam.wca.authentication.filter;

import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.exception.authentication.JwtAuthException;
import com.epam.wca.authentication.service.impl.JwtService;
import com.epam.wca.authentication.service.impl.LoginAttemptService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> uri = Optional.ofNullable(request.getRequestURI());

        if (isUriAllowed(uri) || SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("Allowed URI accessed:");

            filterChain.doFilter(request, response);
            return;
        }

        log.info("Requested URI: {}", uri);

        if (loginAttemptService.isBlocked()) {
            response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            response.getWriter().write("Error: Too much Authentication Attempts. Wait 15 minutes.");
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        try {
            validateBearerTokenIsPresent(authHeader);

            final String jwtToken = extractJwtToken(authHeader);

            String username = jwtService.extractUsername(jwtToken);

            if (username != null) {
                handleAuthentication(request, response, jwtToken, username);
            }

            filterChain.doFilter(request, response);
        } catch (com.epam.wca.authentication.exception.AuthenticationException e) {
            respondWithUnauthorized(
                    response,
                    new JwtAuthException(e.getMessage(), e)
            );
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
            log.error("Authorization Header: {}", authHeader);

            throw new com.epam.wca.authentication.exception.AuthenticationException("Missing or malformed Bearer JWT token.");
        }
    }

    private String extractJwtToken(String authHeader) {
        return authHeader.substring(7); // Remove "Bearer " prefix
    }

    private void handleAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            String jwtToken,
            String username
    ) throws IOException {
        try {
            User user = (User) userDetailsService.loadUserByUsername(username);

            jwtService.validateToken(jwtToken, user);

            setAuthenticationForUser(request, user);
        } catch (com.epam.wca.authentication.exception.AuthenticationException | UsernameNotFoundException e) {
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
