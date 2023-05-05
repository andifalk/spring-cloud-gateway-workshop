# Lab 2: Resilience - Retry, Circuit Breaking and Rate Limiting

In second part we add [resilience design patterns](https://www.codecentric.de/wissens-hub/blog/resilience-design-patterns-retry-fallback-timeout-circuit-breaker) to the gateway.

> **Info:**   
> See [Spring Cloud Gateway Retry](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory),[Spring Cloud Gateway CircuitBreaker](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#spring-cloud-circuitbreaker-filter-factory) and [Spring Cloud Gateway RequestRateLimiter](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory)
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

Productive software has to deliver quality attributes, i.e. it has to be functional correct, reliable, and available.
When it comes to resilience in software design, the main goal is build robust components that can tolerate faults within their scope, but also failures of other components they depend on.
In this lab we take a look at some resilience design patterns: Retry, circuit breaker with fallback and rate limiting.

In lab 2 you will learn how to:

* Add the __Retry__ feature to a request, that is calling a special endpoint in the customer service which is randomly failing
* Configure a [Circuit Breaker](https://dev.to/silviobuss/resilience-pattern-for-java-microservices-the-circuit-breaker-b2g) for calling the customer-service including a __Fallback__ when the call is failing
* Configure a [rate limiter](https://www.cloudflare.com/en-gb/learning/bots/what-is-rate-limiting/) to restrict the amount of requests that can be executed in a certain amount of time.

## Folder Contents

In the lab 2 folder you find 2 applications:

* __initial__: This is the gateway application we will use as starting point for this lab
* __solution__: This is the completed reference solution of the gateway application for this lab including all configured resilience patterns

## Start the Lab

Now, let's start with this lab.

### Explore the initial application

Please navigate your Java IDE to the __lab2/initial/api-gateway__ project and explore this project a bit. Then start the application by running the class `com.example.apigateway.ApiGatewayApplication` inside your IDE or by issuing a `mvnw[.sh|.cmd] spring-boot:run` command.

If you have not yet seen the sample application architecture we will be building starting with this lab then please look into the [sample application architecture](../architecture).

For this lab we will also need the two provided sample backend services that you can find in the _microservices_ root folder:

* product-service: Provides a REST API for products
* customer-service: Provides a REST API for customers

To test if the backend microservice applications works as expected, please run the corresponding spring boot starter classes and check if you can access the following REST API endpoints via the browser or the provided postman collection in _/setup/postman_:

* [localhost:9092/api/v1/products](http://localhost:9091/api/v1/customers)
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

Whenever we assume that an unexpected response (i.e. for a non-reliable service) can be fixed by sending the request again, using the retry pattern can help. It is a very simple pattern where failed requests are retried a configurable number of times in case of a failure before the operation is marked as a failure.

In this step we will try the retry feature with a special API endpoint at http://localhost:9091/api/v1/customers/retry of the customer service that is randomly returning one of the following http status values:

* 200 (OK)
* 400 (Bad Request)
* 500 (Internal Server Error)

So we will add a new route with the _Retry_ filter.

Please open the file `src/main/resources/application.yml` in the _/lab2/initial/api-gateway_ project and add the following entries at the end of the _routes_ path:

application.yml:

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
                statuses: BAD_REQUEST, INTERNAL_SERVER_ERROR
                methods: GET
                backoff:
                  firstBackoff: 10ms
                  maxBackoff: 50ms
                  factor: 3
                  basedOnPreviousValue: false
```

This defines a route from [localhost:9090/api/v1/customers/retry (gateway)](http://localhost:9090/api/v1/customers/retry)  to [localhost:9091//api/v1/customers/retry (customer-service)](http://localhost:9091/api/v1/customers/retry).

The retry filter is configured as follows:

* retries: The number of retries that should be attempted. Here it is tried 5 times.
* statuses: The HTTP status codes that should be retried, her only 400 (Bad request) and 500 (Internal server error) is retried
* methods: The HTTP methods that should be retried, we only want GET requests to be retried.
* backoff: The configured exponential backoff for the retries. Retries are performed after a backoff interval of `firstBackoff * (factor ^ n)`, where _n_ is the iteration. If _maxBackoff_ is configured, the maximum backoff applied is limited to _maxBackoff_. If _basedOnPreviousValue_ is `true`, the _backoff_ is calculated by using `prevBackoff * factor`.

Please see the [Retry GatewayFilter Factory docs](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory) for full parameters description.

Now (re-)start the api-gateway application and make sure you also have started the _customer-service_ microservice located in _/microservices/customer-service_.  
Next, try to call the new route at http://localhost:9090/api/v1/customers/retry using either the web browser or the provided postman collection (corresponding request in folder _Resilience_)

In the logs of the api-gateway you will notice that it might have some entries for retrying to call the customer service because of error results. But the service call should be successful in the end.

```shell
o.s.c.g.f.f.RetryGatewayFilterFactory    : setting new iteration in attr 0
o.s.c.g.f.f.RetryGatewayFilterFactory    : exceedsMaxIterations false, iteration 0, configured retries 5
o.s.c.g.f.f.RetryGatewayFilterFactory    : retryableStatusCode: true, statusCode 400 BAD_REQUEST, configured statuses [400 BAD_REQUEST, 500 INTERNAL_SERVER_ERROR], configured series [SERVER_ERROR]
o.s.c.g.f.f.RetryGatewayFilterFactory    : retryableMethod: true, httpMethod GET, configured methods [GET]
o.s.c.g.f.f.RetryGatewayFilterFactory    : disposing response connection before next iteration
```

### Step 2: Configure a Circuit Breaker

A circuit breaker protects your services from being spammed while already being partly unavailable due to high load. It also protects a service just coming back online from breaking down again when it is flooded with lots of pending requests.  
The circuit breaker pattern was described by [Martin Fowler](https://martinfowler.com/bliki/CircuitBreaker.html). It can be implemented as a stateful software component that switches between three states: closed (requests are performed without any restriction), open (requests are rejected without being submitted to the remote resource), and half-open (one probe request is allowed to decide whether to close the circuit again). Circuit breakers may also be combined with retries, timeouts and fallbacks.

We will now configure a circuit breaker for the routing to the customer service, including both API versions:

* http://localhost:9091/api/v1/customers
* http://localhost:9091/api/v2/customers

The circuit breaker in the spring cloud gateway is implemented using the [Resilience4J library](https://resilience4j.readme.io/docs/circuitbreaker).
So we have to add the corresponding dependencies to the maven _pom.xml_ file:

pom.xml

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

We also added a dependency that introduces a corresponding metric for the Resilience4J circuit breaker.
With this all circuit breaker events can be monitored at http://localhost:9090//actuator/circuitbreakerevents.

After ensuring all prerequisites are now met we can configure the circuit breaker.
Open the file `src/main/resources/application.yml` in the _/lab2/initial/api-gateway_ project and add the following entries in the filters list of the route entry with the _customers_ id:

application.yml:

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

Before we can try the circuit breaker we still have to implement the endpoint for the configured circuit breaker fallback in the api gateway at http://localhost:9090/customer-fallback.

Create the new class `FallbackApi` in the package `com.example.apigateway.resilience` with the following implementation:

FallbackApi.java

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

Her we just have implemented a very simple fallback just informing consumers of the application of a temporary service outage.

Now (re-)start the api-gateway application and make sure you also have started the _customer-service_ microservice located in _/microservices/customer-service_.
Next try to call the route at http://localhost:9090/api/v1/customers or http://localhost:9090/api/v2/customers using either the web browser or the provided postman collection (corresponding requests in folder _routing_). This should work fine with returning the list of customers.  
Now please stop the customer service and try again to call the same requests above. This time the circuit breaker kicks in and the fallback API gets called. So you should see the message above instead.
After restarting the customer service again the call should work fine again, and you should see the customer list.

### Step 3: Configure a rate limiter

In this last step we will introduce a [rate limiter](https://www.cloudflare.com/en-gb/learning/bots/what-is-rate-limiting/) for the product service call.
A rate limiter can support in achieving the following features:

* Mitigate web attacks
  * Brute forcing
  * DoS and DDoS attacks
* Limit the number of requests according to a pricing model  

The standard rate limiter implementation of the spring cloud gateway is the [Redis](https://redis.io/) rate limiter.
That is why a [Redis](https://redis.io/) database is required.

First, we have to add another dependency to the maven _pom.xml_ file:

pom.xml

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

So let's do this (again in the 'application.yml' file):

application.yml:

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


Now (re-)start the api-gateway application and make sure you still have started the _product-service_ microservice located in _/microservices/product-service_.
Next try to call the new route at http://localhost:9090/customers using either the web browser or the provided postman collection (corresponding request in folder _routing_).



<hr>

This ends lab 2. In the next [lab 3](../lab3) you will learn how to configure authentication using [JWT](https://www.codecentric.de/wissens-hub/blog/resilience-design-patterns-retry-fallback-timeout-circuit-breaker) bearer tokens.

> **Important Note:** If you could not finish this lab, then just use the project __lab3/initial/api-gateway__ as new starting point.

<hr>

To continue please head over to [Lab 3](../lab3).
