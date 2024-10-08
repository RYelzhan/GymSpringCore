package com.epam.wca.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.epam.wca.gym")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({JPAConfig.class, WebConfig.class, JacksonConfig.class})
@EnableTransactionManagement
//  TODO: Transaction Testing
public class AppConfig {

    /**
     * Was used to insert Scanner into facade service classes
     * @return Scanner object
     */
    @Deprecated(since = "2.0")
    @Bean
    public Scanner scanner() {
        //  TODO: delete it in future and just use Scanner
        return new Scanner(System.in);
    }

    /**
     * To process @Valid annotation
     * @return LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
