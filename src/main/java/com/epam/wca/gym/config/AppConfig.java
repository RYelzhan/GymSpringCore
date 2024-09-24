package com.epam.wca.gym.config;

import org.springframework.context.annotation.*;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.epam.wca.gym")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({JPAConfig.class})
public class AppConfig {
    @Bean
    public Scanner scanner() {
        //  TODO: delete it in future and just use Scanner
        return new Scanner(System.in);
    }
}
