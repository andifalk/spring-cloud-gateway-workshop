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
public class CustomerService {

    private final CustomerEntityRepository customerEntityRepository;
    private final IdGenerator idGenerator;

    public CustomerService(CustomerEntityRepository customerEntityRepository, IdGenerator idGenerator) {
        this.customerEntityRepository = customerEntityRepository;
        this.idGenerator = idGenerator;
    }

    public List<Customer> findAll() {
        return customerEntityRepository.findAll().stream().map(
                ce -> new Customer(ce.getIdentifier(), ce.getFirstname(), ce.getLastname(), ce.getEmail())
        ).toList();
    }

    public Optional<Customer> findOneByIdentifier(UUID identifier) {
        return customerEntityRepository.findOneByIdentifier(identifier).map(
                ce -> new Customer(ce.getIdentifier(), ce.getFirstname(), ce.getLastname(), ce.getEmail())
        );
    }

    @Transactional
    public Customer save(Customer customer) {

        CustomerEntity customerEntity = customerEntityRepository.save(
                new CustomerEntity(
                        customer.getIdentifier() != null ? customer.getIdentifier() : idGenerator.generateId(),
                        customer.getFirstname(), customer.getLastname(), customer.getEmail())
        );
        return new Customer(
                customerEntity.getIdentifier(), customerEntity.getFirstname(),
                customerEntity.getLastname(), customerEntity.getEmail());
    }
}
