package com.epam.wca.gym.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableConfigurationProperties(ConfigProperties.class)
public class AppConfig {
}
