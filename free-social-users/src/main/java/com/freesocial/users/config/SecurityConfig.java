package com.freesocial.users.config;

import com.freesocial.lib.config.security.DefaultSecurityConfig;
import com.freesocial.lib.config.util.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Defines the configuration for securing internal API access
 */
@Configuration
@Profile("!" + Profiles.TESTS_NO_SECURITY)
public class SecurityConfig extends DefaultSecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        super.prepareDefaultSecurity(http);

        http.authorizeExchange((auth) -> auth
                .pathMatchers("/newuser/**").permitAll()
                .pathMatchers("/users/**").authenticated()
                .anyExchange().denyAll()
        );

        return http.build();
    }

}