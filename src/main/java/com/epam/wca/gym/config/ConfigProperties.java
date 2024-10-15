package com.epam.wca.gym.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// TODO: Use somewhere for practice. E.g.: Swagger

@Configuration
@ConfigurationProperties(prefix = "gym.app")
public class ConfigProperties {
    private String name;
    private String version;
}
