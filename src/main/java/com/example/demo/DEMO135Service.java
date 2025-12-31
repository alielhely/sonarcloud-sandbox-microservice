```java
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFeature {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginFeature() {
        JFrame frame = new JFrame("User Login");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        messageLabel = new JLabel();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(messageLabel);

        frame.add(panel);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void login(String username, String password) {
        if (validateCredentials(username, password)) {
            logLoginAttempt(username, true);
            redirectToDashboard();
        } else {
            logLoginAttempt(username, false);
            messageLabel.setText("Invalid username or password.");
        }
    }

    private boolean validateCredentials(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "user", "password");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            Logger.getLogger(LoginFeature.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    private void redirectToDashboard() {
        // Logic to redirect to the dashboard
        JOptionPane.showMessageDialog(null, "Login successful! Redirecting to dashboard...");
    }

    private void logLoginAttempt(String username, boolean success) {
        // Logic to log login attempts for security
        System.out.println("Login attempt by " + username + ": " + (success ? "Success" : "Failure"));
    }

    public static void main(String[] args) {
        new LoginFeature();
    }
}
```