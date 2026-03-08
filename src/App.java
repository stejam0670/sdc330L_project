import java.util.Scanner;

/**
 * Name: Alex James
 * Date: 2026-03-08
 * Purpose: Entry point for Week 1 submission. Bank account project that
 * demonstrates basic console input/output and inheritance/composition.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Project Week 1 ===");
        System.out.println("Assignment: Bank Account Management (OOP Foundations)");
        System.out.println("Student: Alex James");
        System.out.println();

        System.out.println("Welcome to the Week 1 banking demo.");
        System.out.println("Instructions: enter your first name to begin, then review the sample account output.");
        System.out.print("Enter your first name: ");
        String visitorName = scanner.nextLine().trim();
        if (visitorName.isEmpty()) {
            visitorName = "Guest";
        }

        User user = new User("U1001", "Jordan Miles", "125 Main St", "555-1200", "jordan.miles@email.com");
        Account checking = new CheckingAccount("CHK-2001", 2450.75, user, 300.00);
        user.addAccount(checking);

        System.out.println();
        System.out.println("Hello, " + visitorName + ". Here is the Week 1 class data:");
        System.out.println();
        user.displayUserInfo();
        System.out.println("Account:");
        checking.displayInfo();

        scanner.close();
    }
}
