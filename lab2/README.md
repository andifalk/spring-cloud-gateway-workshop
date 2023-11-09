# Lab 2: Resilience - Retry, Circuit Breaking and Rate Limiting

In the second lab we will add [resilience design patterns](https://www.codecentric.de/wissens-hub/blog/resilience-design-patterns-retry-fallback-timeout-circuit-breaker) to the gateway.

> **Info:**   
> See [Spring Cloud Gateway Retry](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory), [Spring Cloud Gateway CircuitBreaker](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#spring-cloud-circuitbreaker-filter-factory) and [Spring Cloud Gateway RequestRateLimiter](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory)
> for all details on how to configure the corresponding resilience patterns.

## Lab Contents

* [Learning Targets](#learning-targets)
* [Folder Contents](#folder-contents)
* [Tutorial: Resilience - Retry, Circuit Breaking and Rate Limiting](#start-the-lab)
    * [Explore the initial gateway application](#explore-the-initial-application)
    * [Step 1: Retry calling the customer-service](#step-1-retry-the-request-call-to-the-customer-service)
    * [Step 2: Configure a Circuit Breaker](#step-2-configure-a-circuit-breaker)
    * [Step 3: Configure a Rate Limiter](#step-3-configure-a-rate-limiter)

## Learning Targets

Productive software has to deliver quality attributes, i.e. it has to be functional correct, reliable, performant, secure, and available (see [ISO/IEC 25010](https://iso25000.com/index.php/en/iso-25000-standards/iso-25010) for more details on software quality).

When it comes to resilience in software design, the main goal is build robust components that can tolerate faults within their scope, but also failures of other components they depend on.
In this lab we take a look at some resilience design patterns: 
* Retry
* Circuit Breaker with fallback strategy
* Rate Limiting

In lab 2 you will learn how to:

* Add the __Retry__ pattern to a request, that is calling a special endpoint in the customer service which is implemented to fail randomly
* Configure a [Circuit Breaker](https://dev.to/silviobuss/resilience-pattern-for-java-microservices-the-circuit-breaker-b2g) for calling the customer-service including a __Fallback__ when the call is failing
* Configure a [rate limiter](https://www.cloudflare.com/en-gb/learning/bots/what-is-rate-limiting/) to restrict the amount of requests that can be executed in a certain amount of time.

## Folder Contents

In the lab 2 folder you find 2 applications:

* __initial__: This is the gateway application we will use as starting point for this lab
* __solution__: This is the completed reference solution of the gateway application for this lab including all configured resilience patterns

## Start the Lab

Now, let's start with this lab.

### Explore the initial application

Please navigate your Java IDE to the __lab2/initial/api-gateway__ project and explore this project a bit. Then start the application by running the class `com.example.apigateway.ApiGatewayApplication` inside your IDE  
or by issuing a `mvnw[.sh|.cmd] spring-boot:run` command.

If you have not yet seen the sample application architecture we will be building then please have a look into the [sample application architecture](../architecture).

For this lab we will also need the two provided sample backend services that you can find in the _microservices_ root folder:

* __product-service__: Provides a REST API for products
* __customer-service__: Provides a REST API for customers

To test if the backend microservice applications work as expected, please run the corresponding spring boot starter classes and check if you can access the following REST API endpoints via the browser or the provided postman collection in _/setup/postman_:

* [localhost:9091/api/v1/customers](http://localhost:9091/api/v1/customers)
* [localhost:9091/api/v2/customers](http://localhost:9091/api/v2/customers)
* [localhost:9092/api/v1/products](http://localhost:9092/api/v1/products)

You may also use a command-line client as well.
Here are example requests using _httpie_ and _curl_.

Httpie:
```shell
http localhost:9091/api/v1/customers
http localhost:9092/api/v1/products
``` 

Curl:
```shell
curl http://localhost:9091/api/v1/customers
curl http://localhost:9092/api/v1/products
```

<hr>

### Step 1: Retry the request call to the customer-service

Whenever we assume that an unexpected response (i.e. for a non-reliable service) can be fixed by sending the request again, using the _retry_ pattern can help. It is a very simple pattern where failed requests are retried a configurable number of times in case of a failure before the operation is marked as a failure.

In this step we will use the _retry_ feature with a special API endpoint at [http://localhost:9091/api/v1/customers/retry](http://localhost:9091/api/v1/customers/retry) of the customer service that is randomly returning one of the following http status values:

* 200 (OK)
* 400 (Bad Request)
* 408 (Request Timeout)
* 500 (Internal Server Error)
* 503 (Service Unavailable)

Next, we will add a new route including the _Retry_ filter.

Please open the file `src/main/resources/application.yml` in the _/lab2/initial/api-gateway_ project and add the following entries at the end of the `routes` path:

__application.yml:__

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        ...
        - id: retry
          uri: http://localhost:9091
          predicates:
            - Path=/api/v1/customers/retry
          filters:
            - name: Retry
              args:
                retries: 5
                statuses: REQUEST_TIMEOUT, SERVICE_UNAVAILABLE
                methods: GET
                backoff:
                  firstBackoff: 10ms
                  maxBackoff: 50ms
                  factor: 3
                  basedOnPreviousValue: false
```

This defines a route from [localhost:9090/api/v1/customers/retry (gateway)](http://localhost:9090/api/v1/customers/retry)  to [localhost:9091//api/v1/customers/retry (customer-service)](http://localhost:9091/api/v1/customers/retry).

The retry filter is configured as follows:

* __retries__: The number of retries that should be attempted. In our scenario the request is tried to execute 5 times.
* __statuses__: The HTTP status codes that should be retried, in our sample only `408 (REQUEST_TIMEOUT)` and `503 SERVICE_UNAVAILABLE)` is retried
* __methods__: The HTTP methods that should be retried, we only want `GET` requests to be retried.
* __backoff__: The configured exponential backoff for the retries. Retries are performed after a backoff interval of `firstBackoff * (factor ^ n)`, where _n_ is the iteration. If _maxBackoff_ is configured, the maximum backoff applied is limited to _maxBackoff_. If _basedOnPreviousValue_ is `true`, the _backoff_ is calculated by using `prevBackoff * factor`.

Please see the [Retry GatewayFilter Factory docs](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory) for full parameters description.

Now (re-)start the api-gateway application and make sure you also have started the _customer-service_ microservice located in _/microservices/customer-service_.  
Next, try to call the new route at [http://localhost:9090/api/v1/customers/retry](http://localhost:9090/api/v1/customers/retry) using either the web browser or the provided postman collection (corresponding request in folder _Resilience_)

In the logs of the api-gateway you will notice that it might have some entries for retrying to call the customer service because of error results. But the service call should be successful in the end.

```shell
o.s.c.g.f.f.RetryGatewayFilterFactory    : setting new iteration in attr 0
o.s.c.g.f.f.RetryGatewayFilterFactory    : exceedsMaxIterations false, iteration 0, configured retries 5
o.s.c.g.f.f.RetryGatewayFilterFactory    : retryableStatusCode: true, statusCode 503 SERVICE_UNAVAILABLE, configured statuses [403 REQUEST_TIMEOUT, 503 SERVICE_UNAVAILABLE], configured series [SERVER_ERROR]
o.s.c.g.f.f.RetryGatewayFilterFactory    : retryableMethod: true, httpMethod GET, configured methods [GET]
o.s.c.g.f.f.RetryGatewayFilterFactory    : disposing response connection before next iteration
```

### Step 2: Configure a Circuit Breaker

A circuit breaker protects your services from being spammed while already being partly unavailable due to high load. It also protects a service just coming back online from breaking down again when it is flooded with lots of pending requests.  
The circuit breaker pattern was described by [Martin Fowler](https://martinfowler.com/bliki/CircuitBreaker.html). It can be implemented as a stateful software component that switches between three states: closed (requests are performed without any restriction), open (requests are rejected without being submitted to the remote resource), and half-open (one probe request is allowed to decide whether to close the circuit again). Circuit breakers may also be combined with retries, timeouts and fallbacks.

We will now configure a circuit breaker for the routing to the customer service, including both API versions:

* [http://localhost:9091/api/v1/customers](http://localhost:9091/api/v1/customers)
* [http://localhost:9091/api/v2/customers](http://localhost:9091/api/v2/customers)

The circuit breaker in the spring cloud gateway is implemented using the [Resilience4J library](https://resilience4j.readme.io/docs/circuitbreaker).  
So we have to add the corresponding dependencies to the maven `pom.xml` file:

__pom.xml:__

```xml
<dependencies>
    ...
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
    </dependency>
    <dependency>
      <groupId>io.github.resilience4j</groupId>
      <artifactId>resilience4j-micrometer</artifactId>
    </dependency>
    ...
</dependencies>
```

We also have added a dependency that introduces a corresponding metric for the _Resilience4J_ circuit breaker.
With this all circuit breaker events can be monitored at [http://localhost:9090//actuator/circuitbreakerevents](http://localhost:9090//actuator/circuitbreakerevents).

After ensuring all prerequisites are now met we can configure the circuit breaker.
Open the file `src/main/resources/application.yml` in the _/lab2/initial/api-gateway_ project and add the following entries in the filters list of the route entry with the _customers_ id:

__application.yml:__

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        ...
        - id: customers
          uri: http://localhost:9091
          predicates:
            - Path=/api/v1/{segment},/api/v2/{segment}
          filters:
            - name: CircuitBreaker
              args:
                name: myCircuitBreaker
                fallbackUri: forward:/customer-fallback
```

Now a circuit breaker is configured as filter for these routes:
* From [localhost:9090/api/v1/customers (gateway)](http://localhost:9090/api/v1/customers)  to [localhost:9091/api/v1/customers (customer-service)](http://localhost:9091/api/v1/customers)
* From [localhost:9090/api/v2/customers (gateway)](http://localhost:9090/api/v2/customers)  to [localhost:9091/api/v2/customers (customer-service)](http://localhost:9091/api/v2/customers)

Before we can try the circuit breaker we still have to implement the endpoint for the configured circuit breaker fallback in the api gateway at [http://localhost:9090/customer-fallback](http://localhost:9090/customer-fallback).

Create the new class `FallbackApi` in the package `com.example.apigateway.resilience` with the following implementation:

__FallbackApi.java:__

```java
package com.example.apigateway.resilience;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackApi {

    @GetMapping("/customer-fallback")
    ResponseEntity<String> customerFallback() {
        return new ResponseEntity<>("We are sorry, but customer service is currently out of service. \nPlease try later",
                HttpStatusCode.valueOf(503));
    }
}
```

Her we have implemented a very simple fallback just informing consumers of the application of a temporary service outage. Other possible solutions would be to cache a previous result.

Now (re-)start the api-gateway application and make sure you also have started the _customer-service_ microservice located in _/microservices/customer-service_.  
Next try to call the route at http://localhost:9090/api/v1/customers or http://localhost:9090/api/v2/customers using either the web browser or the provided postman collection (corresponding requests in folder _routing_). This should still work fine as before with returning the list of customers.

Now please stop the _customer service_ and try again to call the same requests above. This time the circuit breaker kicks in and the fallback API gets called. So you should see the message of the implemented fallback API endpoint above instead.  
After restarting the _customer service_ again the call should work fine again, and you should see the customer list.

Finally, you may check all circuit breaker events at [http://localhost:9090//actuator/circuitbreakerevents](http://localhost:9090//actuator/circuitbreakerevents).

Next up: Rate limiting.

### Step 3: Configure a rate limiter

In this last step we will introduce a [rate limiter](https://www.cloudflare.com/en-gb/learning/bots/what-is-rate-limiting/) for the product service call.
A rate limiter can support in achieving the following features:

* Mitigate web attacks
  * Brute forcing
  * DoS and DDoS attacks
* Limit the number of requests according to a pricing model  

If a request is blocked by the limiter then the response `HTTP 429 - Too Many Requests` is sent instead of a `HTTP 200 - OK` status.

The standard rate limiter implementation of the spring cloud gateway is the [Redis](https://redis.io/) rate limiter.
That is why a [Redis](https://redis.io/) database is required.

First, we have to add another dependency to the maven `pom.xml` file:

__pom.xml:__

```xml
<dependencies>
    ...
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    </dependency>
    ...
</dependencies>
```

Second, we have to start a local [Redis](https://redis.io/) database.
For this you need to have installed [docker](https://docs.docker.com/get-docker/) and [docker compose](https://docs.docker.com/compose/install/).

Now open a terminal window and navigate to the _lab2/initial_ folder and issue this command:

```shell
docker compose up
```

If this does not work as expected, please re-check that there is the _docker-compose.yml_ file in the current directory.

The final step is the configuration of the rate limiter.  
So let's do this (again in the `application.yml` file):

__application.yml:__

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: products
          uri: http://localhost:9092
          predicates:
            - Path=/api/v1/products
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 10
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@productKeyResolver}"
...
```

The rate limiting algorithm used is the [Token Bucket Algorithm](https://dev.to/satrobit/rate-limiting-using-the-token-bucket-algorithm-3cjh).
The rate limiter defines the following properties:

* __redis-rate-limiter.replenishRate__: Defines how many requests per second to allow (without any dropped requests). This is the rate at which the token bucket is filled.
* __redis-rate-limiter.burstCapacity__: The maximum number of requests a user is allowed in a single second (without any dropped requests). This is the number of tokens the token bucket can hold. Setting this value to zero blocks all requests.
* __redis-rate-limiter.requestedTokens__: Defines how many tokens a request costs. This is the number of tokens taken from the bucket for each request and defaults to 1.
* __rate-limiter__: You can also define your own (optional) implementation of the rate limiter as spring bean.
* __key-resolver__: An (optional) key resolver defines the key for limiting requests.

> See [RequestRateLimiter reference doc](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory) for more details.

As we don't have a Principal (authenticated user) yet (see next lab) it is not possible to use the default `PrincipalNameKeyResolver` implementation of spring cloud gateway.  
Instead, we provide our own simple implementation, limiting the requests based on the request origin address. Our custom resolver is set as bean reference in the configuration above.

Here you find the implementation. Please create a new class `ProductKeyResolver` in the package `com.example.apigateway.resilience`.

__ProductKeyResolver.java:__

```java
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
```

Now (re-)start the api-gateway application and make sure you still have started the _product-service_ microservice located in _/microservices/product-service_.  
Next try to call the new route at [http://localhost:9090/api/v1/products](http://localhost:9090/api/v1/products) using either the web browser or the provided postman collection (corresponding request in folder _Routing_).

To test the rate limiter you need a mechanism to create multiple requests in short time. You won't reach the maximum number of possible requests by manually executing requests in the browser or from postman.  
You may use one of these client tools:

* __Apache Bench__: On Linux and macOS operating systems you may use [Apache Bench (ab)](https://httpd.apache.org/docs/2.4/programs/ab.html). This is a tool from the Apache organization for benchmarking a Hypertext Transfer Protocol (HTTP) web server. With this tool, you can quickly know how many requests per second your web server is capable of serving.
* __Rate Limiter Client__: This workshop also provides a simple client to issue multiple requests to the product service. You find the project for the Rate Limiter Client in the directory _/rate-limiter-client_.

With Apache Bench you can try to perform this command:

```shell
ab -c 2 -m GET -n 10 -v 3 http://localhost:9090/api/v1/products
```

This executes 2 concurrent requests at a time (-c 2), uses the GET HTTP method (-m GET), issues 10 requests (-n 10) and sets a verbose level of `3` to receive HTTP status results.

You could also use [Locust](https://github.com/locustio/locust) for this.
If you already have installed python3 then you just have to perform a `pip install locust` to install locust.

Then use the provided file `locustfile.py` and run `locust -f locustfile.py`
In the [Locust web UI](http://0.0.0.0:8089/) specify the target server http://localhost:9090 of the gateway.

If you cannot install the Apache Bench tool then you may use the _rate-limiter-client_ as an alternative.  
Navigate to the directory _/rate-limiter-client_ and start the class `com.example.client.RateLimiterClientApplication`.

This provides one simple API endpoint:

[http://localhost:9096/api/rate?requests=2&delay=10](http://localhost:9096/api/rate?requests=2&delay=10)

The API requires the following request parameters:

* __requests__: The number of requests to be triggered
* __delay__: The delay to wait between requests (in milliseconds), when `0` is given then no delay is configured

## Configuration

You may configure the base URI and a JWT bearer token in the class `WebClientConfiguration`:

```java
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
```

Independent of the client you are using, if you increase the number of requests, then at some point you will recognize 
that you get the HTTP status of `429(Too Many Requests)` instead of the `200(OK)` HTTP status.

<hr>

This ends lab 2. In the next [lab 3](../lab3) you will learn how to configure authentication using [JWT](https://www.codecentric.de/wissens-hub/blog/resilience-design-patterns-retry-fallback-timeout-circuit-breaker) bearer tokens.

> **Important Note:** If you could not finish this lab, then just use the project __lab3/initial/api-gateway__ as new starting point.

<hr>

To continue please head over to [Lab 3](../lab3).
