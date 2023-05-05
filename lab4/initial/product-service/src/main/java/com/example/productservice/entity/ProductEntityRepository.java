package com.example.productservice.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductEntityRepository  extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findOneByIdentifier(UUID identifier);
    List<ProductEntity> findAllByPremiumIsFalse();
}
