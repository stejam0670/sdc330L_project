import java.util.ArrayList;
import java.util.List;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Stores user information and associated accounts for the Week 2 project.
 */
public class User {
    private String userId;
    private String name;
    private String email;

    // Composition demonstration: User contains a List<Account>.
    private List<Account> accounts;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void displayUserInfo() {
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }

    public void displayAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts assigned.");
            return;
        }

        // Polymorphism demonstration: each account is stored as Account and
        // displayInfo() resolves to the correct subclass version at runtime.
        for (Account account : accounts) {
            account.displayInfo();
        }
    }
}
