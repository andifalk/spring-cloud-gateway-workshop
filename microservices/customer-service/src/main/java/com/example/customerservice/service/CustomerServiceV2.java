package com.example.customerservice.service;

import com.example.customerservice.entity.CustomerEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CustomerServiceV2 {

    private final CustomerEntityRepository customerEntityRepository;
    private final IdGenerator idGenerator;

    public CustomerServiceV2(CustomerEntityRepository customerEntityRepository, IdGenerator idGenerator) {
        this.customerEntityRepository = customerEntityRepository;
        this.idGenerator = idGenerator;
    }

    public List<CustomerV2> findAll() {
        return customerEntityRepository.findAll().stream().map(
                CustomerV2::new
        ).toList();
    }

    public Optional<CustomerV2> findOneByIdentifier(UUID identifier) {
        return customerEntityRepository.findOneByIdentifier(identifier).map(
                CustomerV2::new
        );
    }

    @Transactional
    public CustomerV2 save(CustomerV2 customer) {
        return new CustomerV2(customerEntityRepository.save(customer.toEntity(idGenerator.generateId())));
    }
}
