package com.freesocial.gateway.config.security;

import com.freesocial.gateway.common.enums.AvailableRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.SaveSessionGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

/**
 * Defines the routes in which gateway will proxy requests downstream to services
 * All values in AvailableRoutes will be added since URI field is present
 */
@Configuration
public class SpringCloudConfig {

    @Autowired
    private TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory;

    @Autowired
    private SaveSessionGatewayFilterFactory saveSessionGatewayFilterFactory;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routerBuilder = builder.routes();

        //Adding routes
        Arrays.stream(AvailableRoutes.values())
                .filter(r -> Objects.nonNull(r.getUri())).forEach(routeToDefine -> {
            routerBuilder.route(route -> route.path(routeToDefine.getPath())
                    .filters(f -> f.filter(tokenRelayGatewayFilterFactory.apply())
                            .filter(saveSessionGatewayFilterFactory.apply("")))
                    .uri(routeToDefine.getUri())
            );
        });

        return routerBuilder.build();
    }

}