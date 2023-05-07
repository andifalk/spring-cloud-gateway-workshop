# Lab 4: Security Part 2 - Secure Communication with TLS

In this lab we secure all communication with HTTPS/TLS between clients and the gateway and in between the gateway and backend services.

> See
> * [Spring Cloud TLS and SSL](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#tls-and-ssl)
> * [Spring Boot: Configure SSL](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.webserver.configure-ssl)
> * [Transport Layer Security (TLS)](https://www.cloudflare.com/en-gb/learning/ssl/transport-layer-security-tls/)
>
> for all details on TLS/SSL with spring cloud gateway, TLS/SSL with Spring Boot and transport layer security (TLS) in general.


## Lab Contents

* [Learning Targets](#learning-targets)
* [Folder Contents](#folder-contents)
* [Tutorial: Security Part 2 - Secure Communication with TLS](#start-the-lab)
    * [Explore the initial gateway application](#explore-the-initial-application)
    * [Step 1: Secure communication to the gateway with TLS](#step-1-retry-the-request-call-to-the-customer-service)
    * [Step 2: Secure communication between the gateway and product service with TLS](#step-1-retry-the-request-call-to-the-customer-service)

## Learning Targets

Another cross-cutting feature of an API Gateway is TLS/SSL termination. This way the gateway all communications from clients to backend services are secured at least on its way to the gateway.

In a modern [zero-trust](https://www.ssl.com/blogs/zero-trust-architecture-a-brief-introduction/) approach it is strongly recommended to secure all communication regardless of the network location. So in addition to TLS/SSL termination at the gateway all calls from the gateway to proxied backend services must use secure communication using TLS/SSL.

In lab 4 you will learn how to:

* Configure the gateway to provide the HTTPS protocol instead of HTTP using a trusted self-signed certificate
* Configure one of the backend services as well to use TLS/SSL and change the gateway routes to call the HTTPS endpoints

In this lab you find both the api-gateway and the product-service in the _initial_ and _solution_ folders as we also will change the product-service as part of this lab.

## Folder Contents

In the lab 2 folder you find 2 applications:

* __initial__: This includes the gateway and product-service applications we will use as starting point for this lab
* __solution__: This is the completed reference solution of the gateway and product-service applications for this lab configured using TLS/SSL secured communication

## Start the Lab

Now, let's start with this lab.

### Explore the initial application

Please navigate your Java IDE to the __lab4/initial/api-gateway__ project and explore this project a bit. Then start the application by running the class `com.example.apigateway.ApiGatewayApplication` inside your IDE or by issuing a `mvnw[.sh|.cmd] spring-boot:run` command.

If you have not yet seen the sample application architecture we will be building starting with this lab then please look into the [sample application architecture](../architecture).

In this lab the two provided sample backend services in the _microservices_ root folder will not be required.
Instead, we will use the product-service in the _initial_ folder.

To test if the product-service backend microservice applications works as expected, please run the corresponding spring boot starter class.

> __Note:__ Again please start the application using the `secure` spring profile. By using this profile the application now require a valid JWT to call API endpoints

After starting the applications please check if you can access the following REST API endpoints via the browser or the provided postman collection in _/setup/postman_.

* [localhost:9092/api/v1/products](http://localhost:9092/api/v1/products)

You may also use a command-line client as well.
Here are example requests using _httpie_ and _curl_.

Httpie:
```shell
http localhost:9092/api/v1/products
``` 

Curl:
```shell
curl http://localhost:9092/api/v1/products
```

You will notice that you are not able to call the REST API endpoints successfully. Instead, you get a 401 (Unauthorized) status.
The same will happen if you try to make the calls through the gateway.

Finally, please also make sure you have also set up and started the spring authorization server as this will be required by the gateway and the backend services to validate the tokens.

<hr>

### Step 1: Configure the gateway to communicate via HTTPS (TLS/SSL)

In the first step of this lab we will extend the gateway to act as [OAuth2 resource server](https://www.oauth.com/oauth2-servers/the-resource-server/).

![Gateway_As_Resource_Server](images/resource_server.png)

First, we have to add another dependency for the OAuth resource server spring boot starter feature to the maven _pom.xml_ file:

pom.xml

```xml
<dependencies>
    ...
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
  </dependency>
    ...
</dependencies>
```

The additional dependency adds the [spring security](https://spring.io/projects/spring-security) library and the filter functionality to resolve and validate [bearer tokens](https://oauth.net/2/bearer-tokens/) from the requests.
As we will use [JSON web tokens (JWT)](https://jwt.io/) in our lab, these kind of tokens have to be validated by checking the token signature and the expiry date/time. In our case the token signature is based on [asymmetric cryptography](https://en.wikipedia.org/wiki/Public-key_cryptography), so for validating the signature we need a [public key](https://en.wikipedia.org/wiki/Public_key_infrastructure).  
Next we have to tell the gateway where to retrieve this public key from (it is the spring authorization server that provides this at a publicly accessible endpoint as part of a [JSON Web Key Set](https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets)).

Please open the file `src/main/resources/application.yml` in the _/lab3/initial/api-gateway_ project and add the following entries on the level of the _spring_ path:

application.yml:

```yaml
spring:
  #...
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:9000/oauth2/jwks
```

This defines to load the required public key from [localhost:9000/oauth2/jwks](http://localhost:9000/oauth2/jwks).

Finally, we need some java configuration to enable the authentication for bearer tokens (requiring JWT).

WebSecurityConfiguration.java

```java
package com.example.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {

  @Order(1)
  @Bean
  SecurityWebFilterChain actuatorHttpSecurity(ServerHttpSecurity http) {
    http
            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
            .authorizeExchange((exchanges) -> exchanges
                    .anyExchange().permitAll()
            );
    return http.build();
  }

  @Bean
  SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
    http
            .authorizeExchange((exchanges) -> exchanges
                    .anyExchange().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerSpec::jwt)
            .csrf().disable();
    return http.build();
  }
}
```

### Step 2: Configure the product-service to communicate via HTTPS (TLS/SSL)

As we now have a Principal (authenticated user) it is now possible to use the default `PrincipalNameKeyResolver` implementation of spring cloud gateway.

So let's remove the reference to our own key resolver implementation (again in the 'application.yml' file):

application.yml:

```yaml
spring:
  application:
    name: api-gateway
  #...  
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
...
```

> See [RequestRateLimiter reference doc](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory) for more details.

<hr>

This ends lab 4. In the last [lab 5](../lab5) you will learn how to implement your own customized gateway predicates and filters.

> **Important Note:** If you could not finish this lab, then just use the project __lab5/initial/api-gateway__ as new starting point.

<hr>

To continue please head over to [Lab 5](../lab5).
