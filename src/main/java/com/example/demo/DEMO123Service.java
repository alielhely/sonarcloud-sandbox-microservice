```java
import java.util.Scanner;

public class UserLogin {
    private Database database;
    private Logger logger;

    public UserLogin(Database database, Logger logger) {
        this.database = database;
        this.logger = logger;
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (validateCredentials(username, password)) {
            System.out.println("Login successful! Redirecting to dashboard...");
            redirectToDashboard();
        } else {
            System.out.println("Login failed! Invalid username or password.");
            logger.logFailedAttempt(username);
        }
    }

    private boolean validateCredentials(String username, String password) {
        return database.checkCredentials(username, password);
    }

    private void redirectToDashboard() {
        // Logic to redirect to the user's dashboard
        System.out.println("Welcome to your dashboard!");
    }
}

class Database {
    public boolean checkCredentials(String username, String password) {
        // Simulated database check
        return "user".equals(username) && "pass".equals(password);
    }
}

class Logger {
    public void logFailedAttempt(String username) {
        // Logic to log the failed login attempt
        System.out.println("Failed login attempt for user: " + username);
    }
}

class Main {
    public static void main(String[] args) {
        Database database = new Database();
        Logger logger = new Logger();
        UserLogin userLogin = new UserLogin(database, logger);
        userLogin.login();
    }
}
```