package com.epam.wca.gym.config;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.epam.wca.gym")
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
}
