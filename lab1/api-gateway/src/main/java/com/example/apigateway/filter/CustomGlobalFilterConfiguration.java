package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class CustomGlobalFilterConfiguration implements GlobalFilter {

    private static final Logger LOG = LoggerFactory.getLogger(CustomGlobalFilterConfiguration.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LOG.debug("My Global Filter");
        return chain.filter(exchange);
    }
}
