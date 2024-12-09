package com.epam.wca.authentication.service;

import com.epam.wca.authentication.filter.TransactionIdFilter;
import com.epam.wca.authentication.util.RequestDetailsUtil;
import com.epam.wca.common.gymcommon.util.AppConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final TransactionIdFilter transactionIdFilter;
    private final WebClient webClient;
    public AuthService(WebClient.Builder webClientBuilder, TransactionIdFilter transactionIdFilter) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8095").build();
        this.transactionIdFilter = transactionIdFilter;
    }

    public Mono<Long> authenticate(ServerWebExchange exchange, String authHeader) {
        String transactionId = RequestDetailsUtil.getTransactionId(exchange);

        if (transactionId == null) {
            transactionId = transactionIdFilter.createTransactionId();
            RequestDetailsUtil.setTransactionId(exchange, transactionId);
        }

        return webClient.get()
                .uri("/authenticate")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .header(AppConstants.TRANSACTION_ID_HEADER, transactionId)
                .accept(MediaType.ALL)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody ->
                                        Mono.error(new ResponseStatusException(clientResponse.statusCode(), errorBody
                                )))
                )
                .bodyToMono(Long.class);
    }
}
