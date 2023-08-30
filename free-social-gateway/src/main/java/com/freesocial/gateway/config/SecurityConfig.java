package com.freesocial.gateway.config;

import com.freesocial.gateway.common.enums.AvailableRoutes;
import com.freesocial.gateway.service.UserAuthenticationService;
import com.freesocial.lib.config.security.services.AuthenticationManager;
import com.freesocial.lib.config.security.services.DefaultSecurityConfig;
import com.freesocial.lib.config.security.services.SecurityContextRepository;
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
 * All values in AvailableRoutes will be added, access level will be defined by privateRoute field
 */
@Configuration
@EnableWebFluxSecurity
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