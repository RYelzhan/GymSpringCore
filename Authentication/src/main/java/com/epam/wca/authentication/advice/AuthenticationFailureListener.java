package com.epam.wca.authentication.advice;

import com.epam.wca.authentication.service.impl.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationFailureBadCredentialsEvent e) {
        var request = getCurrentHttpRequest();
        if (request != null) {
            final String xfHeader = request.getHeader("X-Forwarded-For");
            String clientIp;

            if (isHeaderInvalid(request, xfHeader)) {
                clientIp = request.getRemoteAddr();
            } else {
                int clientIpIndex = 0;
                clientIp = xfHeader.split(",")[clientIpIndex];
            }

            loginAttemptService.loginFailed(clientIp);
        }
    }

    private static boolean isHeaderInvalid(HttpServletRequest request, String xfHeader) {
        return xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr());
    }

    private HttpServletRequest getCurrentHttpRequest() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }

        return null;
    }
}
