package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Represents a checking account in the Bank Account Management application.
 */
public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, double balance, AccountHolder holder, double overdraftLimit) {
        super(accountNumber, balance, holder);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String getAccountInfo() {
        return "Checking Account -> " + super.getAccountInfo()
                + ", Overdraft Limit: $" + String.format("%.2f", overdraftLimit);
    }
}
