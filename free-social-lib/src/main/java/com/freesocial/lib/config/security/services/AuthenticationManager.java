package com.freesocial.lib.config.security.services;

import io.jsonwebtoken.Claims;
import com.freesocial.lib.config.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticates the user by checking for the presence of a valid JWT
 */
@Component
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            String authToken = authentication.getCredentials().toString();
            String username = jwtUtil.getUsernameFromToken(authToken);

            if (Boolean.valueOf(jwtUtil.validateToken(authToken, ""))) {
                Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                List<String> rolesMap = claims.get("role", List.class);
                return new UsernamePasswordAuthenticationToken(
                        username, authToken,
                        rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
            }
        } catch (Exception e) {
        }
        return null;
    }
}