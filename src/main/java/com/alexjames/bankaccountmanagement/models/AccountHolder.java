package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Stores account holder information for the Bank Account Management application.
 */
public class AccountHolder {
    // Access specifier demonstration: fields are private and accessed through getters/setters.
    private String name;
    private String email;

    // Constructor demonstration: fields are initialized when the object is created.
    public AccountHolder(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
