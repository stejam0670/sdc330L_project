package com.alexjames.bankaccountmanagement.storage;

import com.alexjames.bankaccountmanagement.models.Account;
import java.util.List;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Defines storage operations for account data.
 */
public interface Storage {
    void save(Account account);

    List<Account> findAll();
}
