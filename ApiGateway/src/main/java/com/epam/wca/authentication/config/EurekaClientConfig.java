package com.epam.wca.authentication.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.epam.wca.authentication.communication")
public class EurekaClientConfig {
}
