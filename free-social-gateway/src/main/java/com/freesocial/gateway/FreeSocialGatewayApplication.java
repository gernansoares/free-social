package com.freesocial.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableWebFlux
@ComponentScan(value = "com.freesocial")
public class FreeSocialGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialGatewayApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }

}
