package com.example.customerservice.service;

import com.example.customerservice.entity.CustomerEntity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CustomerV1 implements Serializable {

    private UUID identifier;
    private String firstname;
    private String lastname;
    private String email;

    @SuppressWarnings("unused")
    public CustomerV1() {}

    public CustomerV1(UUID identifier, String firstname, String lastname, String email) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public CustomerV1(CustomerEntity customerEntity) {
        this.identifier = customerEntity.getIdentifier();
        this.firstname = customerEntity.getFirstname();
        this.lastname = customerEntity.getLastname();
        this.email = customerEntity.getEmail();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerV1 customerV1 = (CustomerV1) o;
        return identifier.equals(customerV1.identifier) && firstname.equals(customerV1.firstname) && lastname.equals(customerV1.lastname) && email.equals(customerV1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, firstname, lastname, email);
    }

    @Override
    public String toString() {
        return "CustomerV1{" +
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
