# Introduction to Spring Cloud Gateway

This section introduces the [API gateway](https://microservices.io/patterns/apigateway.html) pattern and one specific implementation of this pattern: The [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway).

## API Gateways

The granularity of [application programming interfaces (APIs)](https://en.wikipedia.org/wiki/API) provided by microservices is often different from what a client needs. [Microservices](https://microservices.io/patterns/microservices.html) typically provide fine-grained APIs, which means that clients need to interact with multiple services. Additionally different clients need different data or representations.

Typically, the solution for this is to implement an [API gateway](https://microservices.io/patterns/apigateway.html) that is the single entry point for all clients.
An API gateway acts as a reverse proxy to accept all application programming interface (API) calls, aggregate the various services required to fulfill them, and return the appropriate result.  
This way it provides a flexible way of routing requests based on a number of criteria (paths, http headers, etc.).  

It also focuses on cross-cutting concerns such as 
* Security (Authentication and Authorization)
* Resiliency (Rate limiting)
* Monitoring & analytics

![API_Gateway](images/api_gateway.png)

## Spring Cloud Gateway

Spring Cloud Gateway provides a library for building API gateways on top of Spring and Java. It provides a flexible way of routing requests based on a number of criteria, as well as focuses on cross-cutting concerns such as security, resiliency, and monitoring.

The Spring Cloud Gateway is build on Spring Framework 6, Project Reactor and Spring Boot 3 and provides the following features:

* Match routes on any request attribute.
* Route specific predicates and filters
* Circuit Breaker integration.
* Spring Cloud DiscoveryClient integration
* Easy to write Predicates and Filters
* Request Rate Limiting
* Path Rewriting
* Token Propagation (Authentication)
