package com.freesocial.gateway.config;

import com.freesocial.gateway.common.enums.AvailableRoutes;
import com.freesocial.lib.config.security.DefaultSecurityConfig;
import com.freesocial.lib.config.util.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Defines the configuration for securing internal and external API access
 * All values in AvailableRoutes will be added, access level will be defined by privateRoute field
 */
@Configuration
@Profile("!" + Profiles.TESTS_NO_SECURITY)
public class SecurityConfig extends DefaultSecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        super.prepareDefaultSecurity(http);

        //Adding routes
        http.authorizeExchange(auth -> {
                    for (AvailableRoutes route : AvailableRoutes.values()) {

                        if (route.isPrivateRoute()) {
                            auth.pathMatchers(route.getPath()).authenticated();
                        } else {
                            auth.pathMatchers(route.getPath()).permitAll();
                        }
                    }

                    auth.anyExchange().denyAll();
                }
        );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}