package com.freesocial.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@EnableDiscoveryClient
@SpringBootApplication
@EnableWebFlux
@ComponentScan("com.freesocial")
public class FreeSocialSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialSecurityApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }

}