package com.epam.wca.gym.config;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.in_memory_database.DatabaseInitializationPostProcessor;
import com.epam.wca.gym.repository.in_memory_database.InMemoryDatabase;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of beans for the in-memory database.
 *
 * <p>This class is used to configure and manage the in-memory database.
 * It has been deprecated since version 1.1 due to the migration to PostgresSQL
 * with JPA and Hibernate for better scalability and persistence.
 *
 * @deprecated As of version 1.1, use PostgresSQL database configuration with JPA and Hibernate instead.
 * This class remains for backward compatibility but will be removed in future releases.
 * @since 1.0
 */

@Deprecated(since = "1.1", forRemoval = false)

public class StorageConfig {
    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Integer> usernameStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Long> usernameToIdStorage() {
        return new HashMap<>();
    }

    @Bean
    public long[] counterOfId() {
        return new long[]{0};
    }

    @Bean
    public Long[] counterOfIdForTrainings() {
        return new Long[]{0L};
    }

    @Bean
    @DependsOn({"counterOfId", "counterOfIdForTrainings"})
    public DatabaseInitializationPostProcessor databaseInitializationPostProcessor() {
        return new DatabaseInitializationPostProcessor();
    }

    @Bean
    public InMemoryDatabase inMemoryDatabase() {
        return new InMemoryDatabase();
    }
}
