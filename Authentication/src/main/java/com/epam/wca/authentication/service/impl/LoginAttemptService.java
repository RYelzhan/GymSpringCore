package com.epam.wca.authentication.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
// TODO: check if getters are used anywhere
@Getter
@Service
@Slf4j
public class LoginAttemptService {

    @Value("${authentication.starting-attempt}")
    private int startingAttempt;

    @Value("${authentication.max-attempt}")
    private int maxAttempt;

    @Value("${authentication.block-time-millis}")
    private int blockTimeMillis;

    private final Map<String, Integer> attemptsMap;
    private final Map<String, Date> blockTime;

    public LoginAttemptService() {
        attemptsMap = new ConcurrentHashMap<>();
        blockTime = new ConcurrentHashMap<>();
    }

    public void loginFailed(final String key) {
        int attempts = attemptsMap.getOrDefault(key, startingAttempt);

        attempts ++;

        attemptsMap.put(key, attempts);

        if (attempts == maxAttempt) {
            log.info("User got blocked. IP: %s".formatted(key));

            blockTime.put(key, new Date(System.currentTimeMillis() + blockTimeMillis));
        }
    }

    public boolean isBlocked() {
        String clientIp = getClientIP();

        int attempts = attemptsMap.getOrDefault(clientIp, startingAttempt);

        if (attempts >= maxAttempt && blockTime.get(clientIp).before(new Date())) {
            attemptsMap.put(clientIp, startingAttempt);
            attempts = startingAttempt;
        }

        return attempts >= maxAttempt;
    }

    public String getClientIP() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            final String xfHeader = request.getHeader("X-Forwarded-For");
            if (xfHeader != null) {
                int clientIpIndex = 0;
                return xfHeader.split(",")[clientIpIndex];
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
