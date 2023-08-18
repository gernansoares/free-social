package com.freesocial.users.common.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Bean
    private BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String prepareUsername(String username) {
        return username.toLowerCase().replaceAll(" ", "");
    }

    public String encodePassword(String password) {
        return getEncoder().encode(password);
    }

}
