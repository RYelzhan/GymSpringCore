package com.epam.wca.authentication.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.server.ServerWebExchange;

@UtilityClass
public class RequestDetailsUtil {
    public static final String TRANSACTION_ID_KEY = "TRANSACTION_ID";

    public static void setTransactionId(ServerWebExchange exchange, String transactionId) {
        exchange.getAttributes().put(TRANSACTION_ID_KEY, transactionId);
    }

    public static String getTransactionId(ServerWebExchange exchange) {
        return (String) exchange.getAttributes().get(TRANSACTION_ID_KEY);
    }
}
