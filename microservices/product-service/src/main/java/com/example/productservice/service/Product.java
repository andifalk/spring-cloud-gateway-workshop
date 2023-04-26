package com.example.productservice.service;

import com.example.productservice.entity.ProductEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Product extends AbstractPersistable<Long> {

    private @NotNull UUID identifier;
    private @Size(min = 1, max = 50) String name;
    private @Size(min = 1, max = 200) String description;
    private @NotNull BigDecimal price;
    private boolean premium;

    public Product() {
    }

    public Product(ProductEntity productEntity) {
        this.identifier = productEntity.getIdentifier();
        this.name = productEntity.getName();
        this.description = productEntity.getDescription();
        this.price = productEntity.getPrice();
        this.premium = productEntity.isPremium();
    }

    public Product(UUID identifier, String name, String description, BigDecimal price, boolean premium) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.price = price;
        this.premium = premium;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product that = (Product) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(premium, that.premium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, name, description, price, premium);
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "identifier=" + identifier +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price + '\'' +
                ", premium=" + premium +
                '}';
    }

    public ProductEntity toEntity() {
        return new ProductEntity(getIdentifier(), getName(), getDescription(), getPrice(), isPremium());
    }
}
