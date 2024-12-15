package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String reportsFallback() {
        return "Report service is unavailable. Please try again later.";
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String searchFallback() {
        return "Search service is unavailable. Please try again later.";
    }

    @GetMapping("/unmatched")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String unmatchedFallback() {
        return "The requested service is unavailable. Please check the URL and try again.";
    }
}
