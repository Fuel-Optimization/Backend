package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ApiGateway {

    public static void main(String[] args) {
        SpringApplication.run(ApiGateway.class, args);
    }

}
