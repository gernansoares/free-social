package com.freesocial.lib.config.security;

import com.freesocial.lib.common.util.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Defines the configuration for securing internal API access
 * This configuration disables all security and should be used for testing purposes only
 * Can be used by activating Profiles.TESTS_NO_SECURITY in tests classes
 */
@Configuration
@Profile(Profiles.TESTS_NO_SECURITY)
public class SecurityConfigTests extends DefaultSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        super.prepareDefaultSecurity(http);

        http.authorizeExchange((auth) -> auth
                .anyExchange().permitAll()
        );

        return http.build();
    }

}