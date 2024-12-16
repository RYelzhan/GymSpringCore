package com.epam.wca.statistics.interceptor;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.statistics.transaction.UserDetailsContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class UserDetailsInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        String username = request.getHeader(AppConstants.USERNAME_HEADER);

        if (username == null) {
            log.warn("TransactionId: {} | No UserId found in request. URL: {}, HTTP Method: {}",
                    TransactionContext.getTransactionId(), request.getRequestURL(), request.getMethod());

            return false;
        }

        log.info("TransactionId: {} | Found Username in request {}",
                TransactionContext.getTransactionId(), username);

        UserDetailsContext.setUsername(username);

        return true;
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ) {
        UserDetailsContext.clear();
    }
}
