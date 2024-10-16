package com.epam.wca.gym.advice;

import com.epam.wca.gym.service.impl.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationFailureBadCredentialsEvent e) {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            final String xfHeader = request.getHeader("X-Forwarded-For");
            String clientIp;

            if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
                clientIp = request.getRemoteAddr();
            } else {
                clientIp = xfHeader.split(",")[0];
            }

            loginAttemptService.loginFailed(clientIp);
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }

        return null; // No request found
    }
}
