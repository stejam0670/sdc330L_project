package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Provides the abstract base structure for all bank account types.
 */
// Abstraction demonstration: Account defines shared data and behavior for all account types.
public abstract class Account {
    // Access specifier demonstration: private fields hide database identity from direct outside mutation.
    private Long id;

    // Access specifier demonstration: private fields protect internal state.
    private String accountNumber;
    private double balance;

    // Composition demonstration: each Account contains an AccountHolder object.
    private AccountHolder holder;

    // Constructor demonstration: common account data is initialized here.
    public Account(String accountNumber, double balance, AccountHolder holder) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.holder = holder;
    }

    // Constructor demonstration: this overload supports rebuilding an Account from a database record.
    public Account(Long id, String accountNumber, double balance, AccountHolder holder) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.holder = holder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountHolder getHolder() {
        return holder;
    }

    public void setHolder(AccountHolder holder) {
        this.holder = holder;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }

    public String getAccountInfo() {
        return "Account Number: " + accountNumber
                + ", Balance: $" + String.format("%.2f", balance)
                + ", Holder: " + holder.getName()
                + " (" + holder.getEmail() + ")";
    }
}
