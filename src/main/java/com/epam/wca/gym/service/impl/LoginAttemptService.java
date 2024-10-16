package com.epam.wca.gym.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPT = 3;
    private static final int BLOCK_TIME_MILLIS = 300000;
    private final Map<String, Integer> attemptsMap;
    private final Map<String, Date> blockTime;

    public LoginAttemptService() {
        attemptsMap = new ConcurrentHashMap<>();
        blockTime = new ConcurrentHashMap<>();
    }

    public void loginFailed(final String key) {
        int attempts = attemptsMap.getOrDefault(key, 0);

        attempts ++;

        attemptsMap.put(key, attempts);

        if (attempts == MAX_ATTEMPT) {
            blockTime.put(key, new Date(System.currentTimeMillis() + BLOCK_TIME_MILLIS));
        }
    }

    public boolean isBlocked() {
        String clientIp = getClientIP();

        int attempts = attemptsMap.getOrDefault(clientIp, 0);

        if (attempts >= MAX_ATTEMPT && blockTime.get(clientIp).before(new Date())) {
            attemptsMap.put(clientIp, 0);
            attempts = 0;
        }

        return attempts >= MAX_ATTEMPT;
    }

    private String getClientIP() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            final String xfHeader = request.getHeader("X-Forwarded-For");
            if (xfHeader != null) {
                return xfHeader.split(",")[0];
            }
            return request.getRemoteAddr();
        }
        return null;
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }

        return null; // No request found
    }
}
