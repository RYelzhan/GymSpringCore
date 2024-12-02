package com.epam.wca.gym.interceptor;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        TransactionContext.setTransactionId(createTransactionId());

        String transactionId = TransactionContext.getTransactionId();

        log.info("Received request: URL: {}, HTTP Method: {}, transactionId: {}",
                request.getRequestURL(), request.getMethod(), transactionId);

        return true;
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull Object handler,
            Exception exception
    ) {
        String transactionId = TransactionContext.getTransactionId();
        TransactionContext.clear();

        log.info("Completed request: transactionId: {}, Response Status: {}",
                transactionId, response.getStatus());

        if (exception != null) {
            log.error("Exception occurred: {}, transactionId: {}",
                    exception.getMessage(), transactionId);
        }
    }

    private String createTransactionId() {
        return UUID.randomUUID().toString();
    }
}
