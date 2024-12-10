package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.ok("User Service is currently unavailable. Please try again later.");
    }
    @GetMapping("/fallback/search")
    public ResponseEntity<String> search() {
        return ResponseEntity.ok("Searching Service is currently unavailable. Please try again later.");
    }



    @GetMapping("/fallback/unmatched")
    public ResponseEntity<String> globalFallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The requested route is unavailable or does not exist. Please check the URL.");
    }
}
