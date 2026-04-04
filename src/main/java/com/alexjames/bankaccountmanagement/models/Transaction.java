package com.alexjames.bankaccountmanagement.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Name: Alex James
 * Date: 2026-04-04
 * Purpose: Represents a saved account transaction entry.
 */
public class Transaction {
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private Long id;
    private Long accountId;
    private String transactionName;
    private TransactionType transactionType;
    private double amount;
    private LocalDateTime createdAt;

    public Transaction(Long accountId, String transactionName, TransactionType transactionType, double amount, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.transactionName = transactionName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Transaction(Long id, Long accountId, String transactionName, TransactionType transactionType, double amount, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.transactionName = transactionName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtLabel() {
        return createdAt.format(DISPLAY_FORMATTER);
    }
}
