package com.epam.wca.gym.monitoring;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Profile(value = "local")
public class DatabaseInitMonitor implements HealthIndicator {
    private static final String DATABASE_INIT = "Database Initialization";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        if (isHealthGood()) {
            return Health.up().withDetails(Map.of(DATABASE_INIT, "Database was correctly initialised")).build();
        }
        return Health.down().withDetails(Map.of(DATABASE_INIT, "Database was not correctly initialised")).build();
    }

    private boolean isHealthGood() {
        try {
            // Example query: checking if a specific table has rows
            String query = "SELECT COUNT(*) FROM USERS"; // Replace with your table name
            int rowCount = jdbcTemplate.queryForObject(query, Integer.class);

            return rowCount > 0;
        } catch (Exception ex) {
            // In case of any exception (e.g., table not found), return DOWN status
            return false;
        }
    }
}
