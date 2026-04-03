package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Represents a savings account that earns interest.
 */
// Polymorphism demonstration: this class can be treated as Account or InterestEligible.
public class SavingsAccount extends Account implements InterestEligible {
    private double interestRate;

    // Constructor demonstration: used when a new savings account is created from form data.
    public SavingsAccount(String accountNumber, double balance, AccountHolder holder, double interestRate) {
        super(accountNumber, balance, holder);
        this.interestRate = interestRate;
    }

    // Constructor demonstration: used when a savings account is rebuilt from JDBC data.
    public SavingsAccount(Long id, String accountNumber, double balance, AccountHolder holder, double interestRate) {
        super(id, accountNumber, balance, holder);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountTypeLabel() {
        return "Savings";
    }

    @Override
    public String getExtraDetails() {
        return "Interest: " + String.format("%.2f", interestRate) + "%";
    }

    @Override
    public void applyInterest() {
        double interestAmount = getBalance() * (interestRate / 100.0);
        deposit(interestAmount);
    }

    @Override
    public String getAccountInfo() {
        return "Savings Account -> " + super.getAccountInfo()
                + ", Interest Rate: " + String.format("%.2f", interestRate) + "%";
    }
}
