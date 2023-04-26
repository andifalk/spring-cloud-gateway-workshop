# Microservices on the Edge with Spring Cloud Gateway (Hands-On Workshop)

A hands-on workshop to build an API-Gateway using the [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway).

## Table of Contents

* [Workshop Tutorial](#workshop-tutorial)
* [Prerequisites for Participants](prerequisites)
* [Requirements and Setup](#requirements-and-setup)
* [Sample Application Architecture](architecture)
* [Hands-On Workshop](#hands-on-workshop)
  * [Intro Lab](#intro-lab)
    * [Reactive Streams with Spring Reactor in Action](reactive-playground)
  * [Hands-On Labs](#hands-on-labs)
* [Feedback](#feedback)
* [License](#license)

## Workshop Tutorial

To follow the hands-on workshop please open the [workshop tutorial](https://andifalk.gitbook.io/spring-cloud-gateway-workshop/).

## Requirements and Setup

For the hands-on workshop you will extend a provided sample application along with guided tutorials.

You will need a customized version of the [Spring Authorization Server](https://github.com/spring-projects/spring-authorization-server) that you can get from [https://github.com/andifalk/custom-spring-authorization-server](https://github.com/andifalk/custom-spring-authorization-server)

The components you will build (and use) look like this:

![Architecture](/images/demo-architecture.png)

__Please check out the [complete documentation](application-architecture) for the sample application before
starting with the first hands-on lab__.

All code is verified against the currently supported long-term versions 11 and 17 of Java.

To check system requirements and setup for this workshop please follow the [setup guide](setup).

## Hands-On Workshop

### Intro Lab

* [Reactive Streams with Spring Reactor in Action](intro-labs/reactive-streams-intro)

### Hands-On Labs

* [Lab 1: Create a Spring Cloud Gateway application](lab1)
* [Lab 2: Configure & Monitor Routes](lab2/README.md)
* [Lab 3: Rate Limiting and Circuit Breaking](lab3)
* [Lab 4: Security - Authentication with JWT](lab4)
* [Lab 5: Security - TLS/SSL](lab5)
* [Lab 6: Build customized filters](lab6)

## Feedback

Any feedback on this hands-on workshop is highly appreciated.

Just email _andreas.falk(at)novatec-gmbh.de_ or contact me via Twitter (_@andifalk_).

## License

Apache 2.0 licensed

[1]:http://www.apache.org/licenses/LICENSE-2.0.txt
