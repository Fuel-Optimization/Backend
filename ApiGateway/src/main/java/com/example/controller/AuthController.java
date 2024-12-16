package com.example.controller;

import com.example.jwt.JwtUtil;
import com.example.config.UserService;
import com.example.model.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public AuthController(UserService userService, JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getPassword(), "ROLE_USER");
        return "User registered successfully with username: " + user.getEmail();
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest authRequest) {
        User user = userService.loadUserByEmail(authRequest.getUsername());
        if (user != null && userService.isPasswordValid(authRequest.getPassword(), user.getPassword())) {
            // Generate access and refresh tokens
            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
//sendRequestToReportService(accessToken);
            return response;
        }
        throw new RuntimeException("Invalid username or password");
    }
}

class RegisterRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class AuthRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}