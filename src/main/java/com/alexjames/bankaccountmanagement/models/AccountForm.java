package com.alexjames.bankaccountmanagement.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Stores form input data for creating bank accounts through the web application.
 */
public class AccountForm {
    // Access specifier demonstration: form fields are private and accessed through getters/setters.
    @NotBlank(message = "Account holder name is required.")
    private String holderName;

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Please select an account type.")
    private String accountType;

    @NotBlank(message = "Account number is required.")
    private String accountNumber;

    @NotNull(message = "Balance is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative.")
    private Double balance;

    private Double interestRate;

    // Constructor demonstration: a default constructor supports Spring MVC form binding.
    public AccountForm() {
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }
}
