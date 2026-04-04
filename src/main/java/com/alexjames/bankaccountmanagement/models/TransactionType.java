package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-04-04
 * Purpose: Defines the supported balance-changing transaction types.
 */
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
