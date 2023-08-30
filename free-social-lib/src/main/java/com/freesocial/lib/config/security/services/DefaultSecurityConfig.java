package com.freesocial.lib.config.security.services;

import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Defines the configuration for securing internal and external API access
 */
@Configuration
@EnableWebFluxSecurity
public class DefaultSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    public void prepareSecurity(ServerHttpSecurity http) {
        http
                .exceptionHandling(exceptionHandlingSpec ->
                        exceptionHandlingSpec
                                .authenticationEntryPoint((exchange, ex) ->
                                        Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                                )
                                .accessDeniedHandler((exchange, denied) ->
                                        Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))

                .csrf(csrf -> csrf.disable())
                .formLogin(formLoginSpec -> formLoginSpec.disable())
                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository);
    }

}