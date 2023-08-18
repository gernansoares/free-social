package com.freesocial.gateway.config.security;

import com.freesocial.lib.config.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticates the user by checking for the presence of a valid JWT
 */
@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try {
            String authToken = authentication.getCredentials().toString();
            String username = jwtUtil.getUsernameFromToken(authToken);
            return Mono.just(jwtUtil.validateToken(authToken))
                    .filter(valid -> valid)
                    .switchIfEmpty(Mono.empty())
                    .map(valid -> {
                        Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                        List<String> rolesMap = claims.get("role", List.class);
                        return new UsernamePasswordAuthenticationToken(
                                username,
                                authToken,
                                rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        );
                    });
        } catch (Exception e) {
        }
        return Mono.empty();
    }
}