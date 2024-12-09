package com.epam.wca.authentication.config;

import com.epam.wca.authentication.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("gym_application-registration_route",
                        r -> r.path("/gym/authentication/account/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://Gym"))
                .route("gym_application-main_route",
                        r -> r.path("/gym/**")
                                .filters(f -> f.stripPrefix(1).filter(authenticationFilter))
                                .uri("lb://Gym"))
                .route("statistics_route",
                        r -> r.path("/stats/**")
                                .filters(f -> f.stripPrefix(1).filter(authenticationFilter))
                                .uri("lb://Statistics"))
                .route("authentication_route",
                        r -> r.path("/auth/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://Authentication")
                )
                .build();
    }

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters();
    }
}
