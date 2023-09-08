package com.freesocial.lib.config.security;

import com.freesocial.lib.config.exceptions.FreeSocialException;
import com.freesocial.lib.config.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
 * Authenticates the user by checking for the presence of a valid JWT in request
 * that is also registered in database
 */
@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication.getCredentials().toString())
                .flatMap(authToken -> {
                    try {
                        jwtUtil.validateToken(authToken);
                    } catch (JwtException e) {
                        return Mono.empty();
                    }

                    Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                    List<String> rolesMap = claims.get("role", List.class);

                    return Mono.just(new UsernamePasswordAuthenticationToken(
                            jwtUtil.getUsernameFromToken(authToken),
                            authToken,
                            rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                    );
                });

    }
}