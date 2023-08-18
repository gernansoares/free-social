package com.freesocial.users.config;

import com.freesocial.lib.config.security.services.ServiceSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends ServiceSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/newuser/**").permitAll()
                .requestMatchers("/com/freesocial/users/**").authenticated());

        super.prepareFilterChain(httpSecurity);

        return httpSecurity.build();
    }
}