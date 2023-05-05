package com.example.productservice;

import com.example.productservice.entity.ProductEntity;
import com.example.productservice.entity.ProductEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);
    private final ProductEntityRepository productEntityRepository;

    public DataInitializer(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Transactional
    @Override
    public void run(String... args) {
        List<ProductEntity> products = Stream.of(
                new ProductEntity(UUID.randomUUID(), "Apple Juice", "One liter of pure apple juice", new BigDecimal("1.99"), false),
                new ProductEntity(UUID.randomUUID(), "Premium Old Whiskey", "Bottle of exclusive premium 25 year old scotch whiskey", new BigDecimal("550.00"), true),
                new ProductEntity(UUID.randomUUID(), "Orange Juice", "One liter of sweet orange juice", new BigDecimal("2.05"), false),
                new ProductEntity(UUID.randomUUID(), "Lager Beer", "6 bottles of fine lager beer", new BigDecimal("4.99"), false),
                new ProductEntity(UUID.randomUUID(), "Premium Gin", "Bottle of really high premium gin", new BigDecimal("120.00"), true),
                new ProductEntity(UUID.randomUUID(), "Regular Gin", "Bottle of affordable regular gin", new BigDecimal("15.50"), false)
        ).map(productEntityRepository::save).toList();
        LOG.info("Created {} products", products.size());
    }
}
