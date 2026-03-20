/**
 * Name: Alex James
 * Date: 2026-03-19
 * Purpose: Represents a savings account that earns interest.
 */
// Interface implementation: SavingsAccount implements InterestEligible.
public class SavingsAccount extends Account implements InterestEligible {
    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public void applyInterest() {
        double interestAmount = getBalance() * (interestRate / 100.0);
        deposit(interestAmount);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("  Interest Rate: %.2f%%%n", interestRate);
    }
}
