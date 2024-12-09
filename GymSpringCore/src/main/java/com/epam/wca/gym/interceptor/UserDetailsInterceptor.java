package com.epam.wca.gym.interceptor;

import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.transaction.UserDetailsContext;
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
        String idString = request.getHeader(AppConstants.USER_ID_HEADER);

        if (idString == null) {
            log.warn("No UserId found in request. URL: {}, HTTP Method: {}",
                    request.getRequestURL(), request.getMethod());

            return false;
        }

        Long id = Long.valueOf(idString);

        log.info("Found User Id in request {}", id);

        UserDetailsContext.setUserId(id);

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
