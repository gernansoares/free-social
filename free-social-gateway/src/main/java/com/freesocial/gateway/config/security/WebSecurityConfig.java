package com.freesocial.gateway.config.security;

import com.freesocial.gateway.common.enums.AvailableRoutes;
import com.freesocial.gateway.service.UserAuthenticationService;
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
public class WebSecurityConfig {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
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

        //Adding routes
        for (AvailableRoutes route : AvailableRoutes.values()) {
            http.authorizeExchange(auth -> {
                        if (route.isPrivateRoute()) {
                            auth.pathMatchers(route.getPath()).authenticated();
                        } else {
                            auth.pathMatchers(route.getPath()).permitAll();
                        }
                    }
            );
        }

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}