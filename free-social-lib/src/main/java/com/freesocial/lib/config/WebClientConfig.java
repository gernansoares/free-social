package com.freesocial.lib.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    public Flux<String> getAllShops(String token) {
        return webClientBuilder().baseUrl("http://free-social-users")
                .build().get().uri("/users")
                .headers(http -> {
                    System.out.println(token.replaceAll("Bearer ", ""));
                    http.setBearerAuth(token.replaceAll("Bearer ", ""));
                })
                .retrieve()
                .bodyToFlux(String.class);
    }

}
