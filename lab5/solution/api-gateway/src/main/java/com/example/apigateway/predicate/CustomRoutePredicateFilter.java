package com.example.apigateway.predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

@Component
public class CustomRoutePredicateFilter extends AbstractRoutePredicateFactory<CustomRoutePredicateFilter.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRoutePredicateFilter.class);

    public CustomRoutePredicateFilter() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        LOGGER.info("Applying custom routing predicate filter");
        return exchange -> {
            //grab the request
            ServerHttpRequest request = exchange.getRequest();
            //take information from the request to see if it
            //matches configuration.
            return matches(config, request);
        };
    }

    private boolean matches(Config config, ServerHttpRequest request) {
        return false;
    }

    @Override
    public String name() {
        return "Custom Route Filter";
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}
