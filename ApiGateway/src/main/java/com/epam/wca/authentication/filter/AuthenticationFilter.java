package com.epam.wca.authentication.filter;

import com.epam.wca.authentication.service.AuthService;
import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AuthenticationFilter implements GatewayFilter {
    private final AuthService authService;


    public AuthenticationFilter(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    @Logging
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            log.info("Authorization header is empty.");

            return exchange.getResponse().setComplete();
        }

        return authService.authenticate(exchange, authHeader)
                .flatMap(userId -> {
                    // Add userId to the request or context for downstream services
                    ServerHttpRequest modifiedRequest = exchange.getRequest()
                            .mutate()
                            .header(AppConstants.USER_ID_HEADER, userId.toString())
                            .build();

                    log.info("User Id returned from Auth Service: {}", userId);

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                })
                .onErrorResume(ResponseStatusException.class, ex -> {
                    // Propagate error details from the authentication service
                    exchange.getResponse().setStatusCode(ex.getStatusCode());
                    DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(
                            ex.getReason().getBytes(StandardCharsets.UTF_8)
                    );
                    return exchange.getResponse().writeWith(Mono.just(dataBuffer));
                });
    }
}