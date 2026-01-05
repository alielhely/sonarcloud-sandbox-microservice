```java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin {
    private Connection connection;

    public UserLogin(Connection connection) {
        this.connection = connection;
    }

    public String login(String username, String password) {
        if (validateCredentials(username, password)) {
            logLoginAttempt(username, true);
            return redirectToDashboard();
        } else {
            logLoginAttempt(username, false);
            return "Error: Invalid username or password.";
        }
    }

    private boolean validateCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void logLoginAttempt(String username, boolean success) {
        String query = "INSERT INTO login_attempts (username, success, timestamp) VALUES (?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setBoolean(2, success);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String redirectToDashboard() {
        // Logic to redirect to the dashboard
        return "Redirecting to dashboard...";
    }
}
```