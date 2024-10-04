package com.epam.wca.gym.monitoring;

import com.epam.wca.gym.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceMonitor implements HealthIndicator {
    private static final String AUTH_SERVICE = "AuthService";
    private final AuthService authService;

    @Override
    public Health health() {
        if (isHealthGood()) {
            return Health.up().withDetails(Map.of(AUTH_SERVICE, "Service is running")).build();
        }
        return Health.down().withDetails(Map.of(AUTH_SERVICE, "Service is NOT running")).build();
    }

    private boolean isHealthGood() {
        return authService != null;
    }
}
