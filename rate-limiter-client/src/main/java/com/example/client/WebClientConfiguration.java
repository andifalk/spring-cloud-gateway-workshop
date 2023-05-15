package com.example.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl("http://localhost:9090")
                //.defaultHeader("Authorization", "Bearer <JWT>")
                .build();
    }
}
