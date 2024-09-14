package com.epam.wca.gym.config;

import org.springframework.context.annotation.*;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.epam.wca.gym")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}
