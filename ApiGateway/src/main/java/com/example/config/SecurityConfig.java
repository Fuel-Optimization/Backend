package com.example.config;


import com.example.jwt.JwtAuthenticationManager;
import com.example.jwt.JwtSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtSecurityContextRepository jwtSecurityContextRepository;
    private final ReactiveAuthenticationManager authenticationManager;
    public SecurityConfig(JwtAuthenticationManager jwtAuthenticationManager,
                          JwtSecurityContextRepository jwtSecurityContextRepository, ReactiveAuthenticationManager authenticationManager) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtSecurityContextRepository = jwtSecurityContextRepository;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll() // Open paths
                        .anyExchange().authenticated()) // All other requests are secured
                .authenticationManager(jwtAuthenticationManager)
                .securityContextRepository(jwtSecurityContextRepository)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
