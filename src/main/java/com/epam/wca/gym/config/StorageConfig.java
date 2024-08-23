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
        return new HashMap<Long, Trainee>();
    }
    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<Long, Trainer>();
    }
    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<Long, Training>();
    }
}
