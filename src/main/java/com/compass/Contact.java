package com.compass;

import lombok.Getter;

@Getter
public class Contact {
    Long id;
    String firstName;
    String lastName;
    String email;
    String address;
    String zipCode;

    public Contact(Long id, String firstName, String lastName, String email, String address, String zipCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
    }
}