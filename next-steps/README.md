# Next Steps

There are a lot of further things to explore with Spring Cloud Gateway and reactive streams programming.

## Default and Global Filters

There are still more features available like [global filters](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#global-filters) and [default filters]. These kind of filters apply to all routes.

__Default Filters:__

```yaml
spring:
  cloud:
    gateway:
      default-filters:
      - AddResponseHeader=X-My-Response-Default, Default-Value
```

__Global Filters:__

```java
@Configuration
class GlobalFilterConfiguration {

    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }

    public class CustomGlobalFilter implements GlobalFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.info("custom global filter");
            return chain.filter(exchange);
        }

        @Override
        public int getOrder() {
            return -1;
        }
    }
}
```

## Custom Filters

```java
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //If you want to build a "pre" filter you need to manipulate the
            //request before calling chain.filter
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            //use builder to manipulate the request
            return chain.filter(exchange.mutate().request(builder.build()).build());
        };
    }
    
    @Override
    public String name() {
        return "Custom Gateway Filter";
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}
```

## Custom Route Predicates

```java
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
```

<hr>

If you liked the workshop, disliked some part, or you have suggestions for improvement: Any feedback on this hands-on workshop is highly appreciated.  

Just email _andreas.falk(at)novatec-gmbh.de_ or contact me via Twitter (_@andifalk_).

