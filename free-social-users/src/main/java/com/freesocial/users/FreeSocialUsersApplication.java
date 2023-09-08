package com.freesocial.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@EnableDiscoveryClient
@SpringBootApplication
@EnableWebFlux
@ComponentScan("com.freesocial")
public class FreeSocialUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreeSocialUsersApplication.class, args);
		Hooks.enableAutomaticContextPropagation();
	}

}
