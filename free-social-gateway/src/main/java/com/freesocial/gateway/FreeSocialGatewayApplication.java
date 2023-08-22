package com.freesocial.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import reactor.core.publisher.Hooks;

/**
 * Excludes com.freesocial.lib.config.security.services.* cause
 * this package should be used exclusively for backend services securing
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(value = "com.freesocial",
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "com.freesocial.lib.config.security.services.*"))
public class FreeSocialGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialGatewayApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }

}
