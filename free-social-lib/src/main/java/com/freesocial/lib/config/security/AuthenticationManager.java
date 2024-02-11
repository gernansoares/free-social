package com.freesocial.lib.config.security;

import com.freesocial.lib.common.util.Profiles;
import com.freesocial.lib.config.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
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

    @Value("${eureka.client.enabled}")
    private boolean eurekaEnabled;

    /**
     * Verifies if token exists in security service
     * If service discovery is not enabled (tests only) this validation will be ignored
     *
     * @param token token that will be validated
     */
    private Mono<String> validateTokenInService(String token) {
        if (eurekaEnabled) {
            log.debug("Validating token in service {}", token);

            return webClientBuilder
                    .baseUrl("http://free-social-security")
                    .build().get().uri(String.format("/token/%s", token))
                    .retrieve()
                    .toBodilessEntity()
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        log.error("Invalid token in service {}", token);
                        return Mono.empty();
                    })
                    .flatMap(response -> Mono.just(token));
        } else {
            return Mono.just(token);
        }
    }

    /**
     * Validate token integrity
     *
     * @param token token that will be validated
     */
    private Mono<String> validateTokenIntegrity(String token) {
        log.debug("Validating integrity {}", token);

        return Mono.just(token)
                .flatMap(authToken -> {
                    jwtUtil.validateToken(authToken);
                    return Mono.just(authToken);
                })
                .onErrorResume(e -> {
                    log.error("Invalid token integrity  {}", token);
                    return Mono.empty();
                });
    }

    /**
     * Authenticates the user based on its token
     *
     * @param authentication authentication context
     * @return authenticated user
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        log.debug("Authenticating {}", token);

        return Mono.just(authentication.getCredentials().toString())
                .flatMap(authToken -> validateTokenIntegrity(authToken))
                .flatMap(authToken -> validateTokenInService(authToken))
                .flatMap(authToken -> {
                    Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                    List<String> rolesMap = claims.get("role", List.class);

                    log.debug("Token logged in successfully {}", token);

                    return Mono.just(new UsernamePasswordAuthenticationToken(
                            jwtUtil.getUsernameFromToken(authToken),
                            authToken,
                            rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                    );
                });

    }
}