package com.example.customerservice.entity;

import com.example.customerservice.service.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findOneByIdentifier(UUID identifier);
}
