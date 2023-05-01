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
