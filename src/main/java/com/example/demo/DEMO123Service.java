```java
import java.util.Scanner;

public class UserLogin {
    private UserService userService;
    private Logger logger;

    public UserLogin(UserService userService, Logger logger) {
        this.userService = userService;
        this.logger = logger;
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean isValidUser = userService.validateCredentials(username, password);
        logger.logLoginAttempt(username, isValidUser);

        if (isValidUser) {
            redirectToDashboard();
        } else {
            showErrorMessage("Invalid username or password. Please try again.");
        }
    }

    private void redirectToDashboard() {
        System.out.println("Login successful! Redirecting to your dashboard...");
        // Code to redirect to dashboard
    }

    private void showErrorMessage(String message) {
        System.out.println(message);
    }
}

class UserService {
    public boolean validateCredentials(String username, String password) {
        // Logic to validate credentials against the database
        return Database.validateUser(username, password);
    }
}

class Logger {
    public void logLoginAttempt(String username, boolean success) {
        // Logic to log the login attempt
        System.out.println("Login attempt by user: " + username + " was " + (success ? "successful" : "unsuccessful"));
    }
}

class Database {
    public static boolean validateUser(String username, String password) {
        // Dummy validation logic
        return "user".equals(username) && "pass".equals(password);
    }
}
```