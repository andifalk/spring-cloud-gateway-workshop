package com.example.customerservice;

import com.example.customerservice.entity.AddressEntity;
import com.example.customerservice.entity.Country;
import com.example.customerservice.entity.CustomerEntity;
import com.example.customerservice.entity.CustomerEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);
    private final CustomerEntityRepository customerEntityRepository;

    public DataInitializer(CustomerEntityRepository customerEntityRepository) {
        this.customerEntityRepository = customerEntityRepository;
    }

    @Transactional
    @Override
    public void run(String... args) {
        List<CustomerEntity> customers = Stream.of(
                new CustomerEntity(UUID.randomUUID(), "Max", "Maier", "max.maier@example.com",
                        new AddressEntity(UUID.randomUUID(), "Alter Markt 300", "50667", "Cologne", Country.GERMANY)),
                new CustomerEntity(UUID.randomUUID(), "Oliver", "Smith", "oliver.smith@example.com", new AddressEntity(UUID.randomUUID(), "312 Upper Street", "N10NP", "London", Country.UK)),
                new CustomerEntity(UUID.randomUUID(), "Marie", "Gruber", "marie.gruber@example.com", new AddressEntity(UUID.randomUUID(), "Lammgasse 123", "1080", "Vienna", Country.AUSTRIA)),
                new CustomerEntity(UUID.randomUUID(), "Anneli", "Keller", "anneli.keller@example.com", new AddressEntity(UUID.randomUUID(), "Brunngasse 333", "3011", "Bern", Country.SWITZERLAND)),
                new CustomerEntity(UUID.randomUUID(), "Olivia", "Lopez", "olivia.lopez@example.com", new AddressEntity(UUID.randomUUID(), "Placa d'Espanya 456", "08004", "Barcelona", Country.SPAIN))
        ).map(customerEntityRepository::save).toList();
        LOG.info("Created {} customers", customers.size());
    }
}
