/**
 * Name: Alex James
 * Date: 2026-03-19
 * Purpose: Defines the base account type for the Week 2 bank account project.
 */
public abstract class Account {
    private String accountNumber;
    private double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }

    public abstract String getAccountType();

    public void displayInfo() {
        System.out.printf(
                "Type: %s | Account #: %s | Balance: $%.2f%n",
                getAccountType(),
                accountNumber,
                balance);
    }
}
