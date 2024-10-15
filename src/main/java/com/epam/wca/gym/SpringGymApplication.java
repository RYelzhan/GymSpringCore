package com.epam.wca.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: Consider running through Flyway once more and use it for transition between different environments
// TODO: Actuator Health Check should check the data in the database
// TODO: Add Brute Force protector. Block user for 5 minutes on 3 unsuccessful logins
// TODO: Configure CORS policy in Spring Security

@SpringBootApplication
public class SpringGymApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringGymApplication.class, args);
    }
}
