package com.freesocial.lib.config.security.services;

import com.freesocial.lib.config.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authToken = httpServletRequest.getHeader(HEADER_STRING);
            if (Objects.nonNull(authToken)) {

                authToken = authToken.replace(TOKEN_PREFIX, "");
                String username = jwtUtil.getUsernameFromToken(authToken);

                if (jwtUtil.validateToken(authToken, httpServletRequest.getHeader("uuid"))) {
                    Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                    List<String> rolesMap = claims.get("role", List.class);

                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(username,
                            null, rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
