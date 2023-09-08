package com.freesocial.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Hooks;

@EnableEurekaServer
@SpringBootApplication
@ComponentScan("com.freesocial")
public class FreeSocialServiceDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialServiceDiscoveryApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }

}
