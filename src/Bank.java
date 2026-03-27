import java.util.ArrayList;
import java.util.List;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Manages users and accounts for the Week 2 bank account project.
 */
public class Bank {
    private String bankName;
    private List<User> users;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.users = new ArrayList<>();
    }

    public String getBankName() {
        return bankName;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void displayAllUsers() {
        System.out.println("Users at " + bankName + ":");
        for (User user : users) {
            user.displayUserInfo();
            System.out.println();
        }
    }

    public void displayAllAccounts() {
        System.out.println("All accounts at " + bankName + ":");
        for (User user : users) {
            System.out.println("Account owner: " + user.getName());
            user.displayAccounts();
            System.out.println();
        }
    }

    public void applyInterestToEligibleAccounts() {
        System.out.println("Applying interest to eligible accounts:");

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                // Interface and polymorphism demonstration: eligible account types
                // are treated through the InterestEligible interface.
                if (account instanceof InterestEligible) {
                    System.out.println("Applying interest for " + user.getName()
                            + " on account " + account.getAccountNumber() + ".");
                    ((InterestEligible) account).applyInterest();
                }
            }
        }
    }
}
