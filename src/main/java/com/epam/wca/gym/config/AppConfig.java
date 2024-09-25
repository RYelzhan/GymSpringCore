package com.epam.wca.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.epam.wca.gym")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({JPAConfig.class, WebConfig.class})
@EnableTransactionManagement
//  TODO: Transaction Testing
public class AppConfig {
    @Bean
//    @Scope()
    public Scanner scanner() {
        //  TODO: delete it in future and just use Scanner
        return new Scanner(System.in);
    }
}
