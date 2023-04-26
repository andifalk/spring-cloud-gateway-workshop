package com.example.customerservice.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Objects;
import java.util.UUID;

@Entity
public class AddressEntity extends AbstractPersistable<Long> {
    private @NotNull UUID identifier;
    private @Size(min = 1, max = 50) String street;
    private @Size(min = 1, max = 10) String zip;
    private @Size(min = 1, max = 50) String city;
    private @NotNull Country country;

    public AddressEntity() {
    }

    public AddressEntity(UUID identifier, String street, String zip, String city, Country country) {
        this.identifier = identifier;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AddressEntity that = (AddressEntity) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(street, that.street) && Objects.equals(zip, that.zip) && Objects.equals(city, that.city) && country == that.country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, street, zip, city, country);
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "identifier=" + identifier +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", country=" + country +
                '}';
    }
}
