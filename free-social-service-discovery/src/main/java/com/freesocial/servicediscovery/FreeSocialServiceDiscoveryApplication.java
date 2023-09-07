package com.freesocial.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;

@EnableDiscoveryClient
@EnableEurekaServer
@SpringBootApplication
@ComponentScan("com.freesocial")
public class FreeSocialServiceDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialServiceDiscoveryApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }

}
