package com.epam.wca.gym.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "My Gym API",
                description = "API for GYM management",
                version = "2.1"
        )
)
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@Configuration
public class SwaggerConfig {
    @Value("${gym.api.server.url}")
    private String serverUrl;
    @Value("${gym.api.server.description}")
    private String serverDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server =
                new Server()
                        .url(serverUrl)
                        .description(serverDescription);
        return new OpenAPI().servers(List.of(server));
    }
}
