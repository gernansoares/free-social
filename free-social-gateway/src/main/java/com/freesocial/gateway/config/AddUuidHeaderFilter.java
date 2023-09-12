package com.freesocial.gateway.config;

import com.freesocial.lib.config.security.jwt.JwtUtil;
import com.freesocial.lib.config.GlobalContants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Adds logged user's UUID as header in every request to identify users in APIs
 */
@Component
public class AddUuidHeaderFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .flatMap(c -> {
                    Authentication auth = c.getAuthentication();
                    String uuid = jwtUtil.getUuidFromToken(auth.getCredentials().toString());
                    ServerHttpRequest request = exchange.getRequest();
                    request.mutate().header(GlobalContants.HEADER_UUID, uuid);
                    return chain.filter(exchange.mutate().request(request).build());
                })
                .switchIfEmpty(chain.filter(exchange));

    }

    private String insertUuidAsFirstPathParameterInUri(String uri, String path, String uuid) {
        return uri.replace(path, "/" + uuid + path);
    }
}