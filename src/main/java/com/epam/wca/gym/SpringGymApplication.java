package com.epam.wca.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: Consider running through Flyway once more and use it for transition between different environments
// TODO: Add Refresh tokens. They are in database

@SpringBootApplication
public class SpringGymApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringGymApplication.class, args);
    }
}
