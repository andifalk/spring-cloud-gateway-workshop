package com.example.productservice.service;

import com.example.productservice.entity.ProductEntity;
import com.example.productservice.entity.ProductEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductEntityRepository productEntityRepository;

    public ProductService(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    public List<Product> allProducts() {
        return productEntityRepository.findAll().stream().map(Product::new).toList();
    }

    public List<Product> allNonPremiumProducts() {
        return productEntityRepository.findAllByPremiumIsFalse().stream().map(Product::new).toList();
    }

    public Optional<Product> findOneByIdentifier(UUID identifier) {
        return productEntityRepository.findOneByIdentifier(identifier).map(Product::new);
    }
}
