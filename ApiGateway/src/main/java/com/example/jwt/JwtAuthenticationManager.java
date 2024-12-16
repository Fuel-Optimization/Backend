package com.example.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    // Consistent secret key encoded in Base64
    private final byte[] SECRET_KEY_BYTES = "asdsadzxczxczxczxcasdsadedeeeeeeeeeeeeeeeeeeeeeeeeedasdas".getBytes();


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();

        try {
            // Parse the token and extract claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY_BYTES) // Use the consistent secret key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract username and role from claims
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            // Create an Authentication object with extracted details
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );

            return Mono.just(auth); // Return the authentication
        } catch (Exception e) {
            // Log or handle the exception as needed
            return Mono.empty(); // Return an empty Mono if validation fails
        }
    }
}
