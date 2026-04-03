package com.alexjames.bankaccountmanagement.storage;

import com.alexjames.bankaccountmanagement.models.Account;
import java.util.List;
import java.util.Optional;

/**
 * Name: Alex James
 * Date: 2026-04-02
 * Purpose: Defines JDBC-friendly CRUD operations for account data.
 */
public interface Storage {
    Account create(Account account);

    List<Account> findAll();

    Optional<Account> findById(long id);

    void update(Account account);

    void deleteById(long id);
}
