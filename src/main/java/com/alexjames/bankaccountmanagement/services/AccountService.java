package com.alexjames.bankaccountmanagement.services;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.InterestEligible;
import com.alexjames.bankaccountmanagement.storage.Storage;
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
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        Account account = getAccountById(id);
        account.deposit(amount);
        storage.update(account);
        return account;
    }

    public Account withdraw(long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        Account account = getAccountById(id);
        double previousBalance = account.getBalance();
        account.withdraw(amount);

        if (account.getBalance() == previousBalance) {
            throw new IllegalArgumentException("Withdrawal could not be completed.");
        }

        storage.update(account);
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
}
