package com.alexjames.bankaccountmanagement.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

/**
 * Name: Alex James
 * Date: 2026-04-04
 * Purpose: Captures user-entered transaction details for deposits and withdrawals.
 */
public class TransactionForm {
    @NotBlank(message = "Transaction name is required.")
    private String transactionName;

    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero.")
    private Double amount;

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
