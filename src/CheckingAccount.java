/**
 * Name: Alex James
 * Date: 2026-03-08
 * Purpose: Represents a checking account and demonstrates inheritance by
 * extending the base Account class.
 */
// Inheritance demonstration: CheckingAccount inherits from Account.
public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, double balance, User owner, double overdraftLimit) {
        super(accountNumber, balance, owner);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("  Overdraft Limit: $%.2f%n", overdraftLimit);
    }
}
