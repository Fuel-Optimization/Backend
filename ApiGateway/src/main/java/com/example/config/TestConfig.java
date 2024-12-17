//package com.example.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class testConfig {
//    private static final String ApiGateWayAuthToken = "ioj23uheou2982ns132423dq!@#123p82nu218";
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("report-service", r -> r.path("/reports/**")
//                        .filters(f -> f.addRequestHeader("X-Gateway-Token", ApiGateWayAuthToken)
//                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
//                                        .setFallbackUri("forward:/fallback/reports"))
//                                .addRequestParameter("timeout", "5000")) // Timeout in milliseconds
//                        .uri("http://localhost:8086")) // Replace with the actual base URL of your report service
//                .route("search-service", r -> r.path("/search/**")
//                        .filters(f -> f.addRequestHeader("X-Gateway-Token", ApiGateWayAuthToken)
//                                .circuitBreaker(c -> c.setName("userCircuitBreaker")
//                                        .setFallbackUri("forward:/fallback/search"))
//                                .addRequestParameter("timeout", "5000")) // Timeout in milliseconds
//                        .uri("http://localhost:8083")) // Replace with the actual base URL of your search service
//                .route("unmatched-route", r -> r
//                        .path("/**")
//                        .filters(f -> f.rewritePath("/.*", "/fallback/unmatched")) // Rewrite to global fallback
//                        .uri("http://localhost:8080")) // Local server fallback
//                .build();
//    }
//}
