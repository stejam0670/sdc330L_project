import java.util.Scanner;

/**
 * Name: Alex James
 * Date: 2026-03-26
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

        Bank bank = new Bank("Community Trust Bank");

        User userOne = new User("U1001", "Jordan Miles", "jordan.miles@email.com");
        User userTwo = new User("U1002", "Taylor Brooks", "taylor.brooks@email.com");

        Account checking = new CheckingAccount("CHK-2001", 2450.75, 300.00);
        Account savings = new SavingsAccount("SAV-3001", 8900.50, 1.75);
        Account ira = new IRAAccount("IRA-4001", 15400.00, 2.25);
        Account secondChecking = new CheckingAccount("CHK-2002", 1725.20, 250.00);
        Account secondSavings = new SavingsAccount("SAV-3002", 6400.00, 1.50);

        userOne.addAccount(checking);
        userOne.addAccount(savings);
        userOne.addAccount(ira);

        userTwo.addAccount(secondChecking);
        userTwo.addAccount(secondSavings);

        bank.addUser(userOne);
        bank.addUser(userTwo);

        System.out.println();
        System.out.println("Hello, " + visitorName + ". Here is the Week 2 class data:");
        System.out.println("Created bank: " + bank.getBankName());
        System.out.println();

        bank.displayAllUsers();
        bank.displayAllAccounts();

        bank.applyInterestToEligibleAccounts();

        System.out.println();
        System.out.println("Updated balances after interest is applied:");
        bank.displayAllAccounts();

        scanner.close();
    }
}
