package com.example.Aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Aspect
@Component
public class ReportServiceAspect {

    private final RestTemplate restTemplate;

    public ReportServiceAspect(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @AfterReturning(
            pointcut = "execution(* com.example.controller.AuthController.login(..))",
            returning = "response"
    )
    public void afterLogin(Object response) {
        if (response instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> tokenResponse = (Map<String, String>) response;

            // Extract the access token
            String accessToken = tokenResponse.get("accessToken");

            if (accessToken != null) {
                // Send request to REPORTSERVICE
                try {

                    String reportServiceUrl = "http://localhost:8080/reports/driver/3?groupBy=month"; // Replace with actual REPORTSERVICE endpoint

                    // Prepare headers with Authorization token
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + accessToken);

                    // Prepare request entity
                    HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

                    try {
                        // Send request to REPORTSERVICE
                        ResponseEntity<String> responsee = restTemplate.exchange(
                                reportServiceUrl,
                                HttpMethod.GET,
                                requestEntity,
                                String.class
                        );


                    } catch (Exception e) {
                        // Handle errors gracefully
                        System.err.println("Error sending request to REPORTSERVICE: " + e.getMessage());

                    }

                } finally {

                }
            }}}}
