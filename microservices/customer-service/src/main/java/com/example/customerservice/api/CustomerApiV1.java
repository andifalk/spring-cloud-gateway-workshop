package com.example.customerservice.api;

import com.example.customerservice.service.Customer;
import com.example.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(info = @Info(title="Customers API",description = "Customers API", version = "v1"))
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerApiV1 {

    private final CustomerService customerService;

    public CustomerApiV1(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    List<Customer> allCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{customerIdentifier}")
    ResponseEntity<Customer> customer(@PathVariable("customerIdentifier") UUID customerIdentifier) {
        return customerService.findOneByIdentifier(customerIdentifier)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
