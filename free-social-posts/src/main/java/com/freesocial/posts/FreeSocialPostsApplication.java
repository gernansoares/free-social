package com.freesocial.posts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@EnableDiscoveryClient
@SpringBootApplication
@EnableWebFlux
@EnableRetry
@ComponentScan("com.freesocial")
public class FreeSocialPostsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialPostsApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }

}