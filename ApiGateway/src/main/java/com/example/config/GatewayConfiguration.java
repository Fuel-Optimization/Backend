package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.time.Duration;

@Configuration
public class GatewayConfiguration {
    private static final String ApiGateWayAuthToken = "ioj23uheou2982ns132423dq!@#123p82nu218";


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("report-service", r -> r.path("/reports/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Token", ApiGateWayAuthToken)
                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/reports"))
                                .retry(config -> config.setRetries(5) // Increase the number of retries
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.UNAUTHORIZED) // Include 503
                                        .setBackoff(Duration.ofMillis(100), Duration.ofSeconds(2), 2, false)))
                                .uri("lb://REPORTSERVICE"))
                .route("search-service", r -> r.path("/search/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Token", ApiGateWayAuthToken)
                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/search"))
                                .retry(config -> config.setRetries(3).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)))
                        .uri("lb://SEARCHSERVICE"))
                .route("charts-service", r -> r.path("/charts/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Token", ApiGateWayAuthToken)
                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/charts"))
                                .retry(config -> config.setRetries(5)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.SERVICE_UNAVAILABLE)))
                        .uri("lb://CHARTSSERVICE"))
                .route("unmatched-route", r -> r
                        .path("/**")
                        .filters(f -> f
                                .rewritePath("/.*", "/fallback/unmatched")) // Rewrite to global fallback
                        .uri("http://localhost:8080")) // Forward to local server
                .build();
    }
}
