package com.freesocial.users.config;

import com.freesocial.lib.config.security.services.AuthenticationManager;
import com.freesocial.lib.config.security.services.DefaultSecurityConfig;
import com.freesocial.lib.config.security.services.SecurityContextRepository;
import com.freesocial.users.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Defines the configuration for securing internal API access
 */
@Configuration
public class SecurityConfig extends DefaultSecurityConfig {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        super.prepareSecurity(http);

        http.authorizeExchange((auth) -> auth
                .pathMatchers("/newuser/**").permitAll()
                .pathMatchers("/users/**").authenticated()
                .anyExchange().denyAll()
        );

        return http.build();
    }

}