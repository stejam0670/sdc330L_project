package com.alexjames.bankaccountmanagement.services;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.InterestEligible;
import com.alexjames.bankaccountmanagement.models.Transaction;
import com.alexjames.bankaccountmanagement.models.TransactionType;
import com.alexjames.bankaccountmanagement.storage.Storage;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Processes business logic for bank accounts.
 */
@Service
public class AccountService {
    // Access specifier demonstration: the service depends on the Storage abstraction, not a concrete file class.
    private final Storage storage;

    // Constructor demonstration: the service receives its storage dependency here.
    public AccountService(Storage storage) {
        this.storage = storage;
    }

    public Account addAccount(Account account) {
        // Layered architecture: new accounts are inserted through the SQLite storage implementation.
        return storage.create(account);
    }

    public List<Account> getAllAccounts() {
        return storage.findAll();
    }

    public Account getAccountById(long id) {
        return storage.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + id + " was not found."));
    }

    public Account deposit(long id, double amount) {
        return deposit(id, "Deposit", amount);
    }

    public Account deposit(long id, String transactionName, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        Account account = getAccountById(id);
        applyBalanceDelta(account, amount);
        storage.update(account);
        saveTransaction(account.getId(), transactionName, TransactionType.DEPOSIT, amount);
        return account;
    }

    public Account withdraw(long id, double amount) {
        return withdraw(id, "Withdrawal", amount);
    }

    public Account withdraw(long id, String transactionName, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        Account account = getAccountById(id);
        applyBalanceDelta(account, -amount);
        storage.update(account);
        saveTransaction(account.getId(), transactionName, TransactionType.WITHDRAWAL, amount);
        return account;
    }

    public Account updateAccountHolder(long id, String holderName, String holderEmail) {
        Account account = getAccountById(id);
        // Composition demonstration: holder information is updated through the AccountHolder object.
        account.setHolder(new AccountHolder(holderName, holderEmail));
        storage.update(account);
        return account;
    }

    public void deleteAccount(long id) {
        storage.deleteById(id);
    }

    public List<Transaction> getTransactionsForAccount(long accountId) {
        getAccountById(accountId);
        return storage.findTransactionsByAccountId(accountId);
    }

    public Transaction rollbackTransaction(long accountId, long transactionId) {
        Account account = getAccountById(accountId);
        Transaction transaction = storage.findTransactionById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction with id " + transactionId + " was not found."));

        if (!accountIdEquals(accountId, transaction)) {
            throw new IllegalArgumentException("Transaction " + transactionId + " does not belong to account " + accountId + ".");
        }

        applyBalanceDelta(account, reverseTransactionDelta(transaction));
        storage.update(account);
        storage.deleteTransactionById(transactionId);
        return transaction;
    }

    public void applyInterest() {
        List<Account> accounts = storage.findAll();

        for (Account account : accounts) {
            // Polymorphism demonstration: subclasses are processed through the Account base type.
            if (account instanceof InterestEligible interestEligible) {
                interestEligible.applyInterest();
                storage.update(account);
            }
        }
    }

    private void saveTransaction(Long accountId, String transactionName, TransactionType transactionType, double amount) {
        storage.createTransaction(new Transaction(
                accountId,
                transactionName,
                transactionType,
                amount,
                LocalDateTime.now()));
    }

    private boolean accountIdEquals(long accountId, Transaction transaction) {
        return transaction.getAccountId() != null && transaction.getAccountId() == accountId;
    }

    private void applyBalanceDelta(Account account, double delta) {
        account.setBalance(account.getBalance() + delta);
    }

    private double reverseTransactionDelta(Transaction transaction) {
        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            return -transaction.getAmount();
        }

        return transaction.getAmount();
    }
}
