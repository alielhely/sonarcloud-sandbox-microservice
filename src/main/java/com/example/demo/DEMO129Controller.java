package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserRegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password mismatch error");
        }
        if (userService.isEmailRegistered(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered error");
        }
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }
}

@Service
class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, String> userDatabase = new HashMap<>();

    public void registerUser(UserRegistrationRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        userDatabase.put(request.getEmail(), encryptedPassword);
        sendConfirmationEmail(request.getEmail());
    }

    public boolean isEmailRegistered(String email) {
        return userDatabase.containsKey(email);
    }

    private void sendConfirmationEmail(String email) {
        // Simulate sending confirmation email
        System.out.println("Confirmation email sent to " + email);
    }
}

class UserRegistrationRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;

    // Getters and Setters
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