package com.example.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.just(exchange))
                .log()
                .map(serverWebExchange -> {
                    //adds header to response
                    serverWebExchange.getResponse().getHeaders().set("CUSTOM-RESPONSE-HEADER",
                            HttpStatus.OK.equals(serverWebExchange.getResponse().getStatusCode()) ? "It worked": "It did not work");
                    return serverWebExchange;
                })
                .then();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
