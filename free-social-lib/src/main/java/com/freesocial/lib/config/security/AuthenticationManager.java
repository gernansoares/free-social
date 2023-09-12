package com.freesocial.lib.config.security;

import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.config.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * Verifies if token exists in security service
     *
     * @param token token that will be validated
     */
    private Mono<String> validateTokenInService(String token) {
        return webClientBuilder.baseUrl("http://free-social-security")
                .build().get().uri(String.format("/token/%s", token))
                .retrieve()
                .toBodilessEntity()
                .onErrorResume(e -> Mono.empty())
                .flatMap(response -> Mono.just(token));
    }

    /**
     * Validate token integrity
     *
     * @param token token that will be validated
     */
    private Mono<String> validateTokenIntegrity(String token) {
        return Mono.just(token)
                .flatMap(authToken -> {
                    jwtUtil.validateToken(authToken);
                    return Mono.just(authToken);
                })
                .onErrorResume(e -> Mono.empty());
    }

    /**
     * Authenticates the user based on its token
     *
     * @param authentication authentication context
     * @return authenticated user
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication.getCredentials().toString())
                .flatMap(authToken -> validateTokenIntegrity(authToken))
                .flatMap(authToken -> validateTokenInService(authToken))
                .flatMap(authToken -> {
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