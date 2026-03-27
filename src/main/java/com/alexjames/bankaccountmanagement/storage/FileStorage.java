package com.alexjames.bankaccountmanagement.storage;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.CheckingAccount;
import com.alexjames.bankaccountmanagement.models.IRAAccount;
import com.alexjames.bankaccountmanagement.models.SavingsAccount;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Stores and retrieves account data from a text file.
 */
@Repository
public class FileStorage implements Storage {
    private static final String DATA_FILE = "data/accounts.txt";
    private static final String DELIMITER = "\\|";
    private static final String OUTPUT_DELIMITER = "|";

    @Override
    public void save(Account account) {
        // File handling: existing records are read first so the matching account can be updated or appended.
        List<Account> accounts = findAll();
        boolean updated = false;

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(account.getAccountNumber())) {
                accounts.set(i, account);
                updated = true;
                break;
            }
        }

        if (!updated) {
            accounts.add(account);
        }

        writeAccounts(accounts);
    }

    @Override
    public List<Account> findAll() {
        Path path = Paths.get(DATA_FILE);
        List<Account> accounts = new ArrayList<>();

        if (!Files.exists(path)) {
            return accounts;
        }

        try {
            // File handling: each line in the text file is converted back into an Account object.
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (!line.isBlank()) {
                    accounts.add(convertLineToAccount(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read account data from file.", e);
        }

        return accounts;
    }

    private void writeAccounts(List<Account> accounts) {
        Path path = Paths.get(DATA_FILE);
        List<String> lines = new ArrayList<>();

        for (Account account : accounts) {
            lines.add(convertAccountToLine(account));
        }

        try {
            Files.createDirectories(path.getParent());
            // File handling: one account record is written per line using the "|" delimiter.
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write account data to file.", e);
        }
    }

    private String convertAccountToLine(Account account) {
        // Polymorphism demonstration: the correct file format is chosen based on the runtime account subtype.
        if (account instanceof CheckingAccount checkingAccount) {
            return "CHECKING" + OUTPUT_DELIMITER
                    + checkingAccount.getAccountNumber() + OUTPUT_DELIMITER
                    + checkingAccount.getBalance() + OUTPUT_DELIMITER
                    + checkingAccount.getHolder().getName() + OUTPUT_DELIMITER
                    + checkingAccount.getHolder().getEmail() + OUTPUT_DELIMITER
                    + checkingAccount.getOverdraftLimit();
        }

        if (account instanceof SavingsAccount savingsAccount) {
            return "SAVINGS" + OUTPUT_DELIMITER
                    + savingsAccount.getAccountNumber() + OUTPUT_DELIMITER
                    + savingsAccount.getBalance() + OUTPUT_DELIMITER
                    + savingsAccount.getHolder().getName() + OUTPUT_DELIMITER
                    + savingsAccount.getHolder().getEmail() + OUTPUT_DELIMITER
                    + savingsAccount.getInterestRate();
        }

        if (account instanceof IRAAccount iraAccount) {
            return "IRA" + OUTPUT_DELIMITER
                    + iraAccount.getAccountNumber() + OUTPUT_DELIMITER
                    + iraAccount.getBalance() + OUTPUT_DELIMITER
                    + iraAccount.getHolder().getName() + OUTPUT_DELIMITER
                    + iraAccount.getHolder().getEmail() + OUTPUT_DELIMITER
                    + iraAccount.getInterestRate();
        }

        throw new IllegalArgumentException("Unsupported account type.");
    }

    private Account convertLineToAccount(String line) {
        String[] parts = line.split(DELIMITER);

        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid account record: " + line);
        }

        String accountType = parts[0];
        String accountNumber = parts[1];
        double balance = Double.parseDouble(parts[2]);
        AccountHolder holder = new AccountHolder(parts[3], parts[4]);
        double accountValue = Double.parseDouble(parts[5]);

        switch (accountType) {
            case "CHECKING":
                return new CheckingAccount(accountNumber, balance, holder, accountValue);
            case "SAVINGS":
                return new SavingsAccount(accountNumber, balance, holder, accountValue);
            case "IRA":
                return new IRAAccount(accountNumber, balance, holder, accountValue);
            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
    }
}
