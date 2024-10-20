package com.epam.wca.gym.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SecurityScheme(
        name = "jwtToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Value("${gym.api.server.url}")
    private String serverUrl;
    @Value("${gym.api.server.description}")
    private String serverDescription;
    private final ConfigProperties configProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        var server =
                new Server()
                        .url(serverUrl)
                        .description(serverDescription);

        var info = new io.swagger.v3.oas.models.info.Info()
                .title(configProperties.getName())
                .description("API for GYM management")
                .version(configProperties.getVersion());

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
