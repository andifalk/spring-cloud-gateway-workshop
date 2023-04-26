package com.example.customerservice.api;

import com.example.customerservice.service.CustomerServiceV1;
import com.example.customerservice.service.CustomerServiceV2;
import com.example.customerservice.service.CustomerV1;
import com.example.customerservice.service.CustomerV2;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(info = @Info(title="Customers API",description = "Customers API", version = "2"))
@RestController
@RequestMapping("/api/v2/customers")
public class CustomerApiV2 {

    private final CustomerServiceV2 customerServiceV2;

    public CustomerApiV2(CustomerServiceV2 customerServiceV2) {
        this.customerServiceV2 = customerServiceV2;
    }

    @GetMapping
    List<CustomerV2> allCustomersV1() {
        return customerServiceV2.findAll();
    }

    @GetMapping("/{customerIdentifier}")
    ResponseEntity<CustomerV2> customerV1(@PathVariable("customerIdentifier") UUID customerIdentifier) {
        return customerServiceV2.findOneByIdentifier(customerIdentifier)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
