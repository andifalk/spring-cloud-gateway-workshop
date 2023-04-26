package com.example.customerservice;

import com.example.customerservice.entity.AddressEntity;
import com.example.customerservice.entity.Country;
import com.example.customerservice.entity.CustomerEntity;
import com.example.customerservice.entity.CustomerEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerEntityRepository customerEntityRepository;
    private final IdGenerator idGenerator;

    public DataInitializer(CustomerEntityRepository customerEntityRepository, IdGenerator idGenerator) {
        this.customerEntityRepository = customerEntityRepository;
        this.idGenerator = idGenerator;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        List<CustomerEntity> customers = Stream.of(
                new CustomerEntity(idGenerator.generateId(), "Bruce", "Wayne", "bruce.wayne@example.com",
                        new AddressEntity(idGenerator.generateId(), "Wayne-Street 1", "11111", "Batman City", Country.GERMANY)),
                new CustomerEntity(idGenerator.generateId(), "Bruce", "Banner", "bruce.banner@example.com", new AddressEntity(idGenerator.generateId(), "Banner-Street 1", "11111", "Hulk City", Country.GERMANY)),
                new CustomerEntity(idGenerator.generateId(), "Peter", "Parker", "peter.parker@example.com", new AddressEntity(idGenerator.generateId(), "Parker-Street 1", "11111", "Spiderman City", Country.AUSTRIA)),
                new CustomerEntity(idGenerator.generateId(), "Clark", "Kent", "clark.kent@example.com", new AddressEntity(idGenerator.generateId(), "Kent-Street 1", "11111", "Superman City", Country.SWITZERLAND))
        ).map(customerEntityRepository::save).toList();

    }
}
