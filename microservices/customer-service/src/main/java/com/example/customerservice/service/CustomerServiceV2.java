package com.example.customerservice.service;

import com.example.customerservice.entity.CustomerEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CustomerServiceV2 {

    private final CustomerEntityRepository customerEntityRepository;

    public CustomerServiceV2(CustomerEntityRepository customerEntityRepository) {
        this.customerEntityRepository = customerEntityRepository;
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
}
