/**
 * Name: Alex James
 * Date: 2026-03-19
 * Purpose: Represents an IRA account that earns interest.
 */
// Interface implementation: IRAAccount implements InterestEligible.
public class IRAAccount extends Account implements InterestEligible {
    private double interestRate;

    public IRAAccount(String accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "IRA";
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
