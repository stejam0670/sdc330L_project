import java.util.ArrayList;
import java.util.List;

/**
 * Name: Alex James
 * Date: 2026-03-08
 * Purpose: Stores user profile data and demonstrates composition by
 * storing a list of associated bank accounts.
 */
public class User {
    private String userId;
    private String name;
    private String address;
    private String phone;
    private String email;

    // Composition demonstration: User contains a List<Account>.
    private List<Account> accounts;

    public User(String userId, String name, String address, String phone, String email) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void displayUserInfo() {
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
    }
}
