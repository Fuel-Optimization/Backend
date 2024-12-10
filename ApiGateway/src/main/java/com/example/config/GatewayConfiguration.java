package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Set;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/users")
                                        .setStatusCodes(Set.of("404"))))
                        .uri("lb://USERSERVICE"))
                .route("search-service", r -> r.path("/search/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/search")
                        .setStatusCodes(Set.of("404"))))
                        .uri("lb://SEARCHSERVICE"))
                .route("report-service", r -> r.path("/reports/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/report")
                                        .setStatusCodes(Set.of("404"))))
                        .uri("lb://REPORTSERVICE"))
                .route("unmatched-route", r -> r
                        .path("/**") // Matches any request
                        .filters(f -> f
                                .rewritePath("/.*", "/fallback/unmatched")) // Rewrite to global fallback
                        .uri("http://localhost:8080")) // Forward to local server
                .build();
    }
}
