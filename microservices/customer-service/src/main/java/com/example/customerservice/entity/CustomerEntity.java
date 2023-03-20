package com.example.customerservice.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Objects;
import java.util.UUID;

@Entity
public class CustomerEntity extends AbstractPersistable<Long> {

    private @NotNull UUID identifier;
    private @Size(min = 1, max = 50) String firstname;
    private @Size(min = 1, max = 50) String lastname;
    private @Email String email;

    public CustomerEntity() {}

    public CustomerEntity(UUID identifier, String firstname, String lastname, String email) {
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
        if (!super.equals(o)) return false;
        CustomerEntity that = (CustomerEntity) o;
        return identifier.equals(that.identifier) && firstname.equals(that.firstname) && lastname.equals(that.lastname) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, firstname, lastname, email);
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "identifier=" + identifier +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
