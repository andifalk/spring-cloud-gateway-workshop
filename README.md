# Microservices on the Edge with Spring Cloud Gateway (Hands-On Workshop)

A hands-on workshop to build an API-Gateway using the [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway).

__Table of Contents__

* [Workshop Tutorial](#)
* [Requirements and Setup](setup)
* [Hands-On Workshop](#hands-on-workshop)
    * [Intro Labs](#intro-labs)
        * [Reactive Streams with Spring Reactor in Action](intro-labs/reactive-streams-intro)
    * [Hands-On Labs](#hands-on-labs)
        * [Lab 1: Set-Up the API Gateway](lab1)
        * [Lab 2: Add routings to Microservices](lab2)
        * [Lab 3: Add authentication using OAuth2/OIDC and JWT](lab3)
        * [Lab 4: Add resilience using circuite breaker](lab4)
        * [Lab 5: Add rate limiting](lab5)
        * [Lab 6: Build a customized filter](lab6)
        * [Lab 7: Test the gateway functionality](lab7)
* [Slides of workshop](#)
* [Feedback](#feedback)
* [License](#license)

## Workshop Tutorial

To follow the hands-on workshop please open the [workshop tutorial](#).

## Requirements and Setup

For the hands-on workshop you will extend a provided sample application along with guided tutorials.

You will need a customized version of the [Spring Authorization Server](https://github.com/spring-projects/spring-authorization-server) that you can get from [https://github.com/andifalk/custom-spring-authorization-server](https://github.com/andifalk/custom-spring-authorization-server)

The components you will build (and use) look like this:

![Architecture](docs/images/demo-architecture.png)

__Please check out the [complete documentation](application-architecture) for the sample application before
starting with the first hands-on lab__.

All code is verified against the currently supported long-term versions 11 and 17 of Java.

To check system requirements and setup for this workshop please follow the [setup guide](setup).

## Hands-On Workshop

### Intro Labs

* [Reactive Streams with Spring Reactor in Action](intro-labs/reactive-streams-intro)

### Hands-On Labs

* [Lab 1: Set-Up the API Gateway](lab1)
* [Lab 2: Add routings to Microservices](lab2)
* [Lab 3: Add authentication using OAuth2/OIDC and JWT](lab3)
* [Lab 4: Add resilience using circuite breaker](lab4)
* [Lab 5: Add rate limiting](lab5)
* [Lab 6: Build a customized filter](lab6)
* [Lab 7: Test the gateway functionality](lab7)

## Feedback

Any feedback on this hands-on workshop is highly appreciated.

Just send an email to _andreas.falk(at)novatec-gmbh.de_ or contact me via Twitter (_@andifalk_).

## License

Apache 2.0 licensed

[1]:http://www.apache.org/licenses/LICENSE-2.0.txt
