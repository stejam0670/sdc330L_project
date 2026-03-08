/**
 * Name: Alex James
 * Date: 2026-03-08
 * Purpose: Defines the base account type to be inherited by others.
 */
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected User owner;

    public Account(String accountNumber, double balance, User owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public abstract String getAccountType();

    public void displayInfo() {
        System.out.printf(
                "Type: %s | Account #: %s | Owner: %s | Balance: $%.2f%n",
                getAccountType(),
                accountNumber,
                owner.getName(),
                balance);
    }
}
