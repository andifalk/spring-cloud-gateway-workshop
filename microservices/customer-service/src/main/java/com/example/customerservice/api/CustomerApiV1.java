package com.example.customerservice.api;

import com.example.customerservice.service.CustomerV1;
import com.example.customerservice.service.CustomerServiceV1;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@OpenAPIDefinition(info = @Info(title="Customers API",description = "Customers API", version = "1"))
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApiV1 {

    public static final String HIDDEN_API_COOKIE = "hidden-api";
    private final CustomerServiceV1 customerServiceV1;
    private Set<HttpStatusCode> statusCodes;
    private Random random;

    public CustomerApiV1(CustomerServiceV1 customerServiceV1) {
        this.customerServiceV1 = customerServiceV1;
    }

    @PostConstruct
    void init() {
        statusCodes = new HashSet<>();
        statusCodes.add(HttpStatusCode.valueOf(200));
        statusCodes.add(HttpStatusCode.valueOf(400));
        statusCodes.add(HttpStatusCode.valueOf(408));
        statusCodes.add(HttpStatusCode.valueOf(500));
        statusCodes.add(HttpStatusCode.valueOf(503));

        random = new Random();
    }

    @GetMapping
    List<CustomerV1> allCustomersV1() {
        return customerServiceV1.findAll();
    }

    @GetMapping("/{customerIdentifier}")
    ResponseEntity<CustomerV1> customerV1(@PathVariable("customerIdentifier") UUID customerIdentifier) {
        return customerServiceV1.findOneByIdentifier(customerIdentifier)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hidden")
    String hiddenEndpoint(@CookieValue(name = HIDDEN_API_COOKIE, required = false, defaultValue = "false") boolean enableApi) {
        if (enableApi) {
            return "You found the secret hidden endpoint!";
        } else {
            return "Nothing to see here!";
        }
    }

    @GetMapping("/retry")
    ResponseEntity<String> retryEndpoint() {
        HttpStatusCode httpStatusCode = randomStatus();
        if (HttpStatusCode.valueOf(200).isSameCodeAs(httpStatusCode)) {
            return new ResponseEntity<>("Request Success: " + httpStatusCode, httpStatusCode);
        } else {
            return new ResponseEntity<>("Request Failure: " + httpStatusCode, httpStatusCode);
        }
    }

    private HttpStatusCode randomStatus() {
        int randomNumber = random.nextInt(statusCodes.size());
        return statusCodes.toArray(new HttpStatusCode[0])[randomNumber];
    }
}
