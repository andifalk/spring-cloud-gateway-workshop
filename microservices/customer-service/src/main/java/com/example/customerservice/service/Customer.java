package com.example.customerservice.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Customer implements Serializable {

    private UUID identifier;
    private @Size(min = 1, max = 50) String firstname;
    private @Size(min = 1, max = 50) String lastname;
    private @Email String email;

    public Customer() {}

    public Customer(UUID identifier, String firstname, String lastname, String email) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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
        Customer customer = (Customer) o;
        return identifier.equals(customer.identifier) && firstname.equals(customer.firstname) && lastname.equals(customer.lastname) && email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, firstname, lastname, email);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "identifier=" + identifier +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
