package com.epam.wca.apigateway.config;

import com.epam.wca.apigateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("gym_application_route",
                        r -> r.path("/gym/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://Gym"))
                .route("statistics_route",
                        r -> r.path("/stats/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://Statistics"))
                .build();
    }
}
