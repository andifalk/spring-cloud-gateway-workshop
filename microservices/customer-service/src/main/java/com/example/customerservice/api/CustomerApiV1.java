package com.example.customerservice.api;

import com.example.customerservice.service.CustomerV1;
import com.example.customerservice.service.CustomerServiceV1;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(info = @Info(title="Customers API",description = "Customers API", version = "1"))
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApiV1 {

    private final CustomerServiceV1 customerServiceV1;

    public CustomerApiV1(CustomerServiceV1 customerServiceV1) {
        this.customerServiceV1 = customerServiceV1;
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
}
