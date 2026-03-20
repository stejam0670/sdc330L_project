import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Name: Alex James
 * Date: 2026-03-19
 * Purpose: Entry point for the Week 2 bank account management demonstration.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Project Week 2 ===");
        System.out.println("Assignment: Bank Account Management");
        System.out.println("Student: Alex James");
        System.out.println();
        System.out.println("Welcome to the Week 2 banking demo.");
        System.out.println("Instructions: enter your first name, then review the sample account information.");
        System.out.print("Enter your first name: ");

        String visitorName = scanner.nextLine().trim();
        if (visitorName.isEmpty()) {
            visitorName = "Guest";
        }

        User user = new User("U1001", "Jordan Miles", "jordan.miles@email.com");

        Account checking = new CheckingAccount("CHK-2001", 2450.75, 300.00);
        Account savings = new SavingsAccount("SAV-3001", 8900.50, 1.75);
        Account ira = new IRAAccount("IRA-4001", 15400.00, 2.25);

        user.addAccount(checking);
        user.addAccount(savings);
        user.addAccount(ira);

        System.out.println();
        System.out.println("Hello, " + visitorName + ". Here is the Week 2 class data:");
        System.out.println();
        user.displayUserInfo();
        System.out.println();
        System.out.println("Accounts before interest:");

        // Polymorphism demonstration: different account types are stored in a List<Account>.
        List<Account> accounts = new ArrayList<>(user.getAccounts());
        for (Account account : accounts) {
            account.displayInfo();
        }

        // Polymorphism demonstration: different classes are treated as InterestEligible objects.
        List<InterestEligible> interestAccounts = new ArrayList<>();
        interestAccounts.add((InterestEligible) savings);
        interestAccounts.add((InterestEligible) ira);

        for (InterestEligible interestAccount : interestAccounts) {
            interestAccount.applyInterest();
        }

        System.out.println();
        System.out.println("Accounts after interest:");
        for (Account account : accounts) {
            account.displayInfo();
        }

        scanner.close();
    }
}
