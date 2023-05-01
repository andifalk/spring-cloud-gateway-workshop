package com.example.apigateway.resilience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class ProductKeyResolver implements KeyResolver {
    private static final Logger LOG = LoggerFactory.getLogger(ProductKeyResolver.class);

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        LOG.info("Request from {}", Objects.requireNonNull(exchange.getRequest().getLocalAddress().getHostName()));
        return Mono.just(Objects.requireNonNull(exchange.getRequest().getLocalAddress().getHostName()));
    }
}
