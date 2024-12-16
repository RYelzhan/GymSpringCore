package com.epam.wca.authentication.filter;

import com.epam.wca.authentication.util.RequestDetailsUtil;
import com.epam.wca.common.gymcommon.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class TransactionIdFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {
        String transactionId = RequestDetailsUtil.getTransactionId(exchange);

        if (transactionId == null) {
            transactionId = createTransactionId();
            RequestDetailsUtil.setTransactionId(exchange, transactionId);
        }

        var modifiedRequest = exchange.getRequest()
                .mutate()
                .header(AppConstants.TRANSACTION_ID_HEADER, transactionId)
                .build();

        log.info("Transaction Id created: {}", transactionId);

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    public String createTransactionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
