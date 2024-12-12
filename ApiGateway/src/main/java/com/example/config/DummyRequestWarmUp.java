package com.example.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DummyRequestWarmUp implements CommandLineRunner {

    private final WebClient.Builder webClientBuilder;

    public DummyRequestWarmUp(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public void run(String... args) {
        String[] serviceUrls = {
                "http://localhost:8080/reports/dummy", // Dummy endpoint for Report Service
                "http://localhost:8080/search/dummy" , // Dummy endpoint for Search Service
                "http://localhost:8080/reports/dummy", // Dummy endpoint for Report Service
                "http://localhost:8080/search/dummy",
                "http://localhost:8080/reports/ewd",
        };

        for (String serviceUrl : serviceUrls) {
            try {
                System.out.println("Sending dummy request to: " + serviceUrl);
                String response = webClientBuilder.build()
                        .get()
                        .uri(serviceUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                System.out.println("Response from dummy request: " + response);
            } catch (Exception e) {
                System.err.println("Failed dummy request for: " + serviceUrl + ", error: " + e.getMessage());
            }
        }
    }
}
