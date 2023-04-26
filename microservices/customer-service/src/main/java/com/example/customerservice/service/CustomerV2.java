package com.example.customerservice.service;

import com.example.customerservice.entity.CustomerEntity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CustomerV2 implements Serializable {

    private UUID identifier;
    private String firstname;
    private String lastname;
    private String email;
    private Address address;

    @SuppressWarnings("unused")
    public CustomerV2() {}

    public CustomerV2(UUID identifier, String firstname, String lastname, String email, Address address) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
    }

    public CustomerV2(CustomerEntity customerEntity) {
        this.identifier = customerEntity.getIdentifier();
        this.firstname = customerEntity.getFirstname();
        this.lastname = customerEntity.getLastname();
        this.email = customerEntity.getEmail();
        this.address = new Address(customerEntity.getAddress());
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerV2 customerV2 = (CustomerV2) o;
        return identifier.equals(customerV2.identifier) && firstname.equals(customerV2.firstname) && lastname.equals(customerV2.lastname) && email.equals(customerV2.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, firstname, lastname, email);
    }

    @Override
    public String toString() {
        return "CustomerV2{" +
                "identifier=" + identifier +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public CustomerEntity toEntity(UUID identifier) {
        return new CustomerEntity(getIdentifier() == null ? identifier : getIdentifier(),
                getFirstname(), getLastname(), getEmail(), null);
    }
}
