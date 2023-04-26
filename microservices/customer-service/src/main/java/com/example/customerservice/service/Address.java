package com.example.customerservice.service;

import com.example.customerservice.entity.AddressEntity;
import com.example.customerservice.entity.Country;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Address implements Serializable {
    private UUID identifier;
    private String street;
    private String zip;
    private String city;
    private Country country;

    public Address() {
    }

    public Address(UUID identifier, String street, String zip, String city, Country country) {
        this.identifier = identifier;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    public Address(AddressEntity addressEntity) {
        this.identifier = addressEntity.getIdentifier();
        this.street = addressEntity.getStreet();
        this.zip = addressEntity.getZip();
        this.city = addressEntity.getCity();
        this.country = addressEntity.getCountry();
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
        Address that = (Address) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(street, that.street) && Objects.equals(zip, that.zip) && Objects.equals(city, that.city) && country == that.country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, street, zip, city, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                "identifier=" + identifier +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", country=" + country +
                '}';
    }

    public AddressEntity toEntity() {
        return new AddressEntity(this.identifier, this.street, this.zip, this.city, this.country);
    }
}
