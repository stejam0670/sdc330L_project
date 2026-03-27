package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Represents an IRA account that earns interest.
 */
// Polymorphism demonstration: this class can be treated as Account or InterestEligible.
public class IRAAccount extends Account implements InterestEligible {
    private double interestRate;

    public IRAAccount(String accountNumber, double balance, AccountHolder holder, double interestRate) {
        super(accountNumber, balance, holder);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public void applyInterest() {
        double interestAmount = getBalance() * (interestRate / 100.0);
        deposit(interestAmount);
    }

    @Override
    public String getAccountInfo() {
        return "IRA Account -> " + super.getAccountInfo()
                + ", Interest Rate: " + String.format("%.2f", interestRate) + "%";
    }
}
