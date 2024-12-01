package com.example.model.Repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.model.Repository") // Point to Common module
@EntityScan(basePackages = "com.example.model") // Point to shared entities
@ComponentScan(basePackages = "com.example")
public class OdbListenerService {

    public static void main(String[] args) {
        SpringApplication.run(OdbListenerService.class, args);
    }

}
