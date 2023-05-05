# Lab 3: Security Part 1 - Authentication with JWT

In the second lab we add [resilience design patterns](https://www.codecentric.de/wissens-hub/blog/resilience-design-patterns-retry-fallback-timeout-circuit-breaker) to the gateway.

> **Info:**   
> See [Spring Cloud Gateway Retry](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory),[Spring Cloud Gateway CircuitBreaker](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#spring-cloud-circuitbreaker-filter-factory) and [Spring Cloud Gateway RequestRateLimiter](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory)
> for all details on how to configure the corresponding resilience patterns.

## Lab Contents

* [Learning Targets](#learning-targets)
* [Folder Contents](#folder-contents)
* [Tutorial: Resilience - Retry, Circuit Breaking and Rate Limiting](#start-the-lab)
    * [Explore the initial gateway application](#explore-the-initial-application)
    * [Step 1: Retry calling the customer-service](#step-1-retry-the-request-call-to-the-customer-service)

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


<hr>

This ends lab 3. In the next [lab 4](../lab4) you will learn how to configure secure communication for requests using [TLS](https://www.cloudflare.com/en-gb/learning/ssl/transport-layer-security-tls/).

> **Important Note:** If you could not finish this lab, then just use the project __lab4/initial/api-gateway__ as new starting point.

<hr>

To continue please head over to [Lab 4](../lab4).