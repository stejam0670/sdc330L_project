package com.alexjames.bankaccountmanagement.storage;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.CheckingAccount;
import com.alexjames.bankaccountmanagement.models.IRAAccount;
import com.alexjames.bankaccountmanagement.models.SavingsAccount;
import com.alexjames.bankaccountmanagement.models.Transaction;
import com.alexjames.bankaccountmanagement.models.TransactionType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Name: Alex James
 * Date: 2026-04-02
 * Purpose: Stores and retrieves account data in an SQLite database using plain JDBC.
 */
@Repository
public class SQLiteStorage implements Storage {
    /*
     * Database interaction using JDBC:
     * This SQL creates the accounts table if it does not already exist.
     */
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_number TEXT NOT NULL,
                account_type TEXT NOT NULL,
                holder_name TEXT NOT NULL,
                holder_email TEXT NOT NULL,
                balance REAL NOT NULL,
                interest_rate REAL,
                overdraft_limit REAL
            )
            """;

    private static final String INSERT_ACCOUNT_SQL = """
            INSERT INTO accounts (
                account_number,
                account_type,
                holder_name,
                holder_email,
                balance,
                interest_rate,
                overdraft_limit
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String CREATE_TRANSACTIONS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_id INTEGER NOT NULL,
                transaction_name TEXT NOT NULL,
                transaction_type TEXT NOT NULL,
                amount REAL NOT NULL,
                created_at TEXT NOT NULL,
                FOREIGN KEY (account_id) REFERENCES accounts (id)
            )
            """;

    private static final String SELECT_ALL_SQL = """
            SELECT id, account_number, account_type, holder_name, holder_email, balance, interest_rate, overdraft_limit
            FROM accounts
            ORDER BY id
            """;

    private static final String SELECT_BY_ID_SQL = """
            SELECT id, account_number, account_type, holder_name, holder_email, balance, interest_rate, overdraft_limit
            FROM accounts
            WHERE id = ?
            """;

    private static final String UPDATE_ACCOUNT_SQL = """
            UPDATE accounts
            SET account_number = ?,
                account_type = ?,
                holder_name = ?,
                holder_email = ?,
                balance = ?,
                interest_rate = ?,
                overdraft_limit = ?
            WHERE id = ?
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM accounts
            WHERE id = ?
            """;

    private static final String INSERT_TRANSACTION_SQL = """
            INSERT INTO transactions (
                account_id,
                transaction_name,
                transaction_type,
                amount,
                created_at
            ) VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SELECT_TRANSACTIONS_BY_ACCOUNT_ID_SQL = """
            SELECT id, account_id, transaction_name, transaction_type, amount, created_at
            FROM transactions
            WHERE account_id = ?
            ORDER BY created_at DESC, id DESC
            """;

    private static final String SELECT_TRANSACTION_BY_ID_SQL = """
            SELECT id, account_id, transaction_name, transaction_type, amount, created_at
            FROM transactions
            WHERE id = ?
            """;

    private static final String DELETE_TRANSACTION_BY_ID_SQL = """
            DELETE FROM transactions
            WHERE id = ?
            """;

    // Access specifier demonstration: private state keeps JDBC configuration inside the storage class.
    private final String databaseUrl;

    // Constructor demonstration: Spring injects the configured database path when this repository is created.
    public SQLiteStorage(@Value("${storage.sqlite.database-path:data/bank.db}") String databasePath) {
        this.databaseUrl = "jdbc:sqlite:" + databasePath;
        initializeDatabase(databasePath);
    }

    @Override
    public Account create(Account account) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bindCommonFields(statement, account);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getLong(1));
                }
            }

            return account;
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to insert account into SQLite.", exception);
        }
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(mapRowToAccount(resultSet));
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to read accounts from SQLite.", exception);
        }

        return accounts;
    }

    @Override
    public Optional<Account> findById(long id) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToAccount(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to read account " + id + " from SQLite.", exception);
        }
    }

    @Override
    public void update(Account account) {
        if (account.getId() == null) {
            throw new IllegalArgumentException("Account id is required for update operations.");
        }

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_SQL)) {
            bindCommonFields(statement, account);
            statement.setLong(8, account.getId());

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalArgumentException("Account with id " + account.getId() + " was not found.");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to update account " + account.getId() + " in SQLite.", exception);
        }
    }

    @Override
    public void deleteById(long id) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setLong(1, id);

            int deletedRows = statement.executeUpdate();
            if (deletedRows == 0) {
                throw new IllegalArgumentException("Account with id " + id + " was not found.");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to delete account " + id + " from SQLite.", exception);
        }
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, transaction.getAccountId());
            statement.setString(2, transaction.getTransactionName());
            statement.setString(3, transaction.getTransactionType().name());
            statement.setDouble(4, transaction.getAmount());
            statement.setString(5, transaction.getCreatedAt().toString());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getLong(1));
                }
            }

            return transaction;
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to insert transaction into SQLite.", exception);
        }
    }

    @Override
    public List<Transaction> findTransactionsByAccountId(long accountId) {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_ACCOUNT_ID_SQL)) {
            statement.setLong(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapRowToTransaction(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to read transactions for account " + accountId + " from SQLite.", exception);
        }

        return transactions;
    }

    @Override
    public Optional<Transaction> findTransactionById(long transactionId) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID_SQL)) {
            statement.setLong(1, transactionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToTransaction(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to read transaction " + transactionId + " from SQLite.", exception);
        }
    }

    @Override
    public void deleteTransactionById(long transactionId) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION_BY_ID_SQL)) {
            statement.setLong(1, transactionId);

            int deletedRows = statement.executeUpdate();
            if (deletedRows == 0) {
                throw new IllegalArgumentException("Transaction with id " + transactionId + " was not found.");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to delete transaction " + transactionId + " from SQLite.", exception);
        }
    }

    private void initializeDatabase(String databasePath) {
        try {
            Path parentPath = Paths.get(databasePath).toAbsolutePath().getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
        } catch (Exception exception) {
            throw new RuntimeException("Unable to create the SQLite data directory.", exception);
        }

        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_SQL);
            statement.execute(CREATE_TRANSACTIONS_TABLE_SQL);
        } catch (SQLException exception) {
            throw new RuntimeException("Unable to initialize the SQLite database.", exception);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private void bindCommonFields(PreparedStatement statement, Account account) throws SQLException {
        statement.setString(1, account.getAccountNumber());
        statement.setString(2, determineAccountType(account));
        statement.setString(3, account.getHolder().getName());
        statement.setString(4, account.getHolder().getEmail());
        statement.setDouble(5, account.getBalance());
        statement.setDouble(6, extractInterestRate(account));
        statement.setDouble(7, extractOverdraftLimit(account));
    }

    /*
     * Abstraction and polymorphism:
     * A single JDBC row is converted into the correct Account subclass based on the account_type column.
     */
    private Account mapRowToAccount(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String accountNumber = resultSet.getString("account_number");
        String accountType = resultSet.getString("account_type");
        String holderName = resultSet.getString("holder_name");
        String holderEmail = resultSet.getString("holder_email");
        double balance = resultSet.getDouble("balance");
        double interestRate = resultSet.getDouble("interest_rate");
        double overdraftLimit = resultSet.getDouble("overdraft_limit");

        AccountHolder accountHolder = new AccountHolder(holderName, holderEmail);
        String normalizedType = accountType.toUpperCase(Locale.ROOT);

        return switch (normalizedType) {
            case "CHECKING" -> new CheckingAccount(id, accountNumber, balance, accountHolder, overdraftLimit);
            case "SAVINGS" -> new SavingsAccount(id, accountNumber, balance, accountHolder, interestRate);
            case "IRA" -> new IRAAccount(id, accountNumber, balance, accountHolder, interestRate);
            default -> throw new IllegalArgumentException("Unknown account type in database: " + accountType);
        };
    }

    private String determineAccountType(Account account) {
        if (account instanceof CheckingAccount) {
            return "CHECKING";
        }

        if (account instanceof SavingsAccount) {
            return "SAVINGS";
        }

        if (account instanceof IRAAccount) {
            return "IRA";
        }

        throw new IllegalArgumentException("Unsupported account type: " + account.getClass().getSimpleName());
    }

    private Transaction mapRowToTransaction(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long accountId = resultSet.getLong("account_id");
        String transactionName = resultSet.getString("transaction_name");
        TransactionType transactionType = TransactionType.valueOf(resultSet.getString("transaction_type"));
        double amount = resultSet.getDouble("amount");
        LocalDateTime createdAt = LocalDateTime.parse(resultSet.getString("created_at"));

        return new Transaction(id, accountId, transactionName, transactionType, amount, createdAt);
    }

    private double extractInterestRate(Account account) {
        if (account instanceof SavingsAccount savingsAccount) {
            return savingsAccount.getInterestRate();
        }

        if (account instanceof IRAAccount iraAccount) {
            return iraAccount.getInterestRate();
        }

        return 0.0;
    }

    private double extractOverdraftLimit(Account account) {
        if (account instanceof CheckingAccount checkingAccount) {
            return checkingAccount.getOverdraftLimit();
        }

        return 0.0;
    }
}
