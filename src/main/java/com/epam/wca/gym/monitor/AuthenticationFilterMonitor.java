package com.epam.wca.gym.monitor;

import com.epam.wca.gym.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationFilterMonitor implements HealthIndicator {
    private final static String AUTH_FILTER = "Authentication Filter";
    private final AuthenticationFilter authenticationFilter;

    @Override
    public Health health() {
        if (isHealthGood()) {
            return Health.up().withDetails(Map.of(AUTH_FILTER, "Filter is running")).build();
        }
        return Health.down().withDetails(Map.of(AUTH_FILTER, "Filter is NOT running")).build();
    }

    private boolean isHealthGood() {
        return authenticationFilter != null;
    }
}
