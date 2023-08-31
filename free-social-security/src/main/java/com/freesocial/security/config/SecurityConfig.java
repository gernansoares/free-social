package com.freesocial.security.config;

import com.freesocial.lib.config.security.AuthenticationManager;
import com.freesocial.lib.config.security.DefaultSecurityConfig;
import com.freesocial.lib.config.security.SecurityContextRepository;
import com.freesocial.security.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Defines the configuration for securing internal and external API access
 * All values in AvailableRoutes will be added, access level will be defined by privateRoute field
 */
@Configuration
@Profile("!tests-no-security")
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

        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/logout/**").authenticated()
                .anyExchange().denyAll()
        );

        return http.build();
    }

}