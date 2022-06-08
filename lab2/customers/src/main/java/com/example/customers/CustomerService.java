package com.example.customers;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private static final UUID CUSTOMER_1 = UUID.fromString("e2c4fb04-49c4-435c-815a-123cb8041058");
    private static final UUID CUSTOMER_2 = UUID.fromString("616bc923-c240-46b2-8e26-efd6e4036871");
    private static final UUID CUSTOMER_3 = UUID.fromString("522af857-dc40-4969-bbbb-08239872cefe");

    private final List<Customer> customers = List.of(
        new Customer(CUSTOMER_1, "Peter", "Parker"),
        new Customer(CUSTOMER_2, "Clark", "Kent"),
        new Customer(CUSTOMER_3, "Bruce", "Wayne")
    );

    Flux<Customer> allCustomers() {
        return Flux.fromIterable(customers);
    }

    Mono<Customer> findCustomerByIdentifier(UUID identifier) {
        return Flux.fromIterable(customers).filter(c -> c.getIdentifier().equals(identifier)).singleOrEmpty();
    }
    Flux<Customer> findCustomersByLastName(String lastName) {
        return Flux.fromIterable(customers).filter(c -> c.getLastName().equals(lastName));
    }
}
