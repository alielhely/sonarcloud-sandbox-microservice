package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/register")
class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Map<String, String> registerUser(@RequestBody UserRegistrationRequest request) {
        Map<String, String> response = new HashMap<>();
        
        if (!isValidEmail(request.getEmail())) {
            response.put("error", "Invalid email address");
            return response;
        }
        
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("error", "Password mismatch");
            return response;
        }
        
        if (userService.isEmailRegistered(request.getEmail())) {
            response.put("error", "Email already registered");
            return response;
        }
        
        userService.registerUser(request.getEmail(), request.getPassword());
        response.put("message", "User registered successfully. Confirmation email sent.");
        return response;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}

@Service
class UserService {

    private final Map<String, String> userDatabase = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean isEmailRegistered(String email) {
        return userDatabase.containsKey(email);
    }

    public void registerUser(String email, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        userDatabase.put(email, encryptedPassword);
        sendConfirmationEmail(email);
    }

    private void sendConfirmationEmail(String email) {
        // Simulate sending an email
        System.out.println("Confirmation email sent to " + email);
    }
}

class UserRegistrationRequest {
    private String email;
    private String password;
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}