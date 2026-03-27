package com.alexjames.bankaccountmanagement.services;

import com.alexjames.bankaccountmanagement.models.Account;
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
    private final Storage storage;

    // Constructor demonstration: the service receives its storage dependency here.
    public AccountService(Storage storage) {
        this.storage = storage;
    }

    public void addAccount(Account account) {
        // Layered architecture: business logic passes account data to the storage layer.
        storage.save(account);
    }

    public List<Account> getAllAccounts() {
        return storage.findAll();
    }

    public void applyInterest() {
        List<Account> accounts = storage.findAll();

        for (Account account : accounts) {
            // Polymorphism demonstration: subclasses are processed through the Account base type.
            if (account instanceof InterestEligible interestEligible) {
                interestEligible.applyInterest();
                storage.save(account);
            }
        }
    }
}
