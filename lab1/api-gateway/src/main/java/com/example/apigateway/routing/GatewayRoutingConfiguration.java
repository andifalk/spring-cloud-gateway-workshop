package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutingConfiguration {

    @Bean
    RouteLocator routing(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route("http-bin", ps -> ps.path("/get")
                        .filters(f -> f.addResponseHeader("X-MyResponseHeader", "my response")
                                .addRequestHeader("X-MyRequestHeader", "my request"))
                                .uri("https://httpbin.org"))
                .route("http-bin-rewrite", ps -> ps.path("/bin")
                        .filters(f -> f.rewritePath("/bin/?(?<segment>.*)", "/get"))
                        .uri("https://httpbin.org"))
                .build();
    }

}
