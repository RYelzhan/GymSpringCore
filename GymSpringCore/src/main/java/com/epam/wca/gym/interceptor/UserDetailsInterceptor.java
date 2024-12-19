package com.epam.wca.gym.interceptor;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.transaction.UserDetailsContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDetailsInterceptor implements HandlerInterceptor {
    private Set<String> allowedPrefixes;

    @Value("${allowed.prefixes}")
    public void setAllowedPrefixes(String[] prefixes) {
        allowedPrefixes = Arrays.stream(prefixes).collect(Collectors.toSet());
    }

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (isUriAllowed(request)) {
            return true;
        }

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

    private boolean isUriAllowed(HttpServletRequest request) {
        Optional<String> uri = Optional.ofNullable(request.getRequestURI());
        return uri.isPresent() && allowedPrefixes.stream().anyMatch(uri.get()::startsWith);
    }
}
