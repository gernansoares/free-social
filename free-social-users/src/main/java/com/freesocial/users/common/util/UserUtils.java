package com.freesocial.users.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    private static BCryptPasswordEncoder staticEncoder;

    @Autowired
    public void setEncoder(BCryptPasswordEncoder encoder) {
        staticEncoder = encoder;
    }

    public static String prepareUsername(String username) {
        return username.toLowerCase().replaceAll(" ", "");
    }

    public static String encodePassword(String password) {
        return staticEncoder.encode(password);
    }

}
