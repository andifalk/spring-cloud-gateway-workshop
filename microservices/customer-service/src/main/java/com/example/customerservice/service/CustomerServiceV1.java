package com.example.customerservice.service;

import com.example.customerservice.entity.CustomerEntity;
import com.example.customerservice.entity.CustomerEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CustomerServiceV1 {

    private final CustomerEntityRepository customerEntityRepository;
    private final IdGenerator idGenerator;

    public CustomerServiceV1(CustomerEntityRepository customerEntityRepository, IdGenerator idGenerator) {
        this.customerEntityRepository = customerEntityRepository;
        this.idGenerator = idGenerator;
    }

    public List<CustomerV1> findAll() {
        return customerEntityRepository.findAll().stream().map(
                CustomerV1::new
        ).toList();
    }

    public Optional<CustomerV1> findOneByIdentifier(UUID identifier) {
        return customerEntityRepository.findOneByIdentifier(identifier).map(
                ce -> new CustomerV1(ce.getIdentifier(), ce.getFirstname(), ce.getLastname(), ce.getEmail())
        );
    }

    @Transactional
    public CustomerV1 save(CustomerV1 customer) {
        return new CustomerV1(customerEntityRepository.save(customer.toEntity(idGenerator.generateId())));
    }
}
