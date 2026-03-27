/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Represents a checking account for the Week 2 project.
 */
// Inheritance demonstration: CheckingAccount inherits from Account.
public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, double balance, double overdraftLimit) {
        super(accountNumber, balance);
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
