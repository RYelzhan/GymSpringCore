package com.epam.wca.authentication.interceptor;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Enumeration;

import static com.epam.wca.common.gymcommon.util.AppConstants.TRANSACTION_ID_HEADER;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws IOException {
        String transactionId = request.getHeader(TRANSACTION_ID_HEADER);

        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            System.out.println("Request Headers:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headerValues = request.getHeaders(headerName);

                System.out.print(headerName + ": ");
                while (headerValues.hasMoreElements()) {
                    System.out.print(headerValues.nextElement());
                    if (headerValues.hasMoreElements()) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("No headers present in the request.");
        }

        if (transactionId == null) {
            log.warn("No transactionId found in request. URL: {}, HTTP Method: {}",
                    request.getRequestURL(), request.getMethod());

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("No transactionId found in request.");

            return false;
        }

        TransactionContext.setTransactionId(transactionId);

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
}
