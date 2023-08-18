package com.freesocial.lib.config.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ServiceSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Prepares the httpsecurity with all the boilerplate configuration which should be defined
     * in each backend service for security, each service must define its own requestMatchers
     * @param httpSecurity used to defined security configuration
     * @throws Exception if errors occurs while authorizing requests
     */
    public void prepareFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((auth) -> auth
                .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .exceptionHandling(exceptionHandler ->
                        exceptionHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable());
    }

}