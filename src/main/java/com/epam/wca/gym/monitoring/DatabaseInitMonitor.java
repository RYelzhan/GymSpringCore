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
    private static final String USERS_TABLE = "users_table";
    private static final String TRAINEES_TABLE = "trainees_table";
    private static final String TRAINERS_TABLE = "trainers_table";
    private static final String TRAININGS_TABLE = "trainings_table";
    private static final String TRAINING_TYPE_TABLE = "training_type_table";
    private static final String QUERY_SELECT_COUNT_FROM = "SELECT COUNT(*) FROM %s";
    private static final String DATABASE_INITIALIZED = "Database was correctly initialised";
    private static final String DATABASE_NOT_INITIALIZED = "Database was not correctly initialised";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        if (isHealthGood()) {
            Map<String, Integer> details = Map.of(
                    USERS_TABLE, getTableRowCount(USERS_TABLE),
                    TRAINEES_TABLE, getTableRowCount(TRAINEES_TABLE),
                    TRAINERS_TABLE, getTableRowCount(TRAINERS_TABLE),
                    TRAININGS_TABLE, getTableRowCount(TRAININGS_TABLE),
                    TRAINING_TYPE_TABLE , getTableRowCount(TRAINING_TYPE_TABLE)
            );

            return Health.up()
                    .withDetails(Map.of(DATABASE_INIT, DATABASE_INITIALIZED))
                    .withDetails(details)
                    .build();
        }
        return Health
                .down()
                .withDetails(Map.of(DATABASE_INIT, DATABASE_NOT_INITIALIZED))
                .build();
    }

    private boolean isHealthGood() {
        try {
            String query = QUERY_SELECT_COUNT_FROM.formatted(USERS_TABLE);
            Integer rowCount = jdbcTemplate.queryForObject(query, Integer.class);

            return rowCount != null && rowCount > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    private Integer getTableRowCount(String tableName) {
        String query = QUERY_SELECT_COUNT_FROM.formatted(tableName);
        try {
            return jdbcTemplate.queryForObject(query, Integer.class);
        } catch (Exception e) {
            return -1;
        }
    }
}

