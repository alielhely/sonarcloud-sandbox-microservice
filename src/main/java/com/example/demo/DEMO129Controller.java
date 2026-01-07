package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;
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
@RequestMapping("/api/registration")
class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Map<String, String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        Map<String, String> response = new HashMap<>();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("message", "Password mismatch");
            return response;
        }

        if (userService.isEmailRegistered(request.getEmail())) {
            response.put("message", "Email already registered");
            return response;
        }

        if (!userService.isPasswordStrong(request.getPassword())) {
            response.put("message", "Weak password");
            return response;
        }

        userService.registerUser(request);
        response.put("message", "Registration successful");
        return response;
    }
}

class UserRegistrationRequest {
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;

    // Getters and Setters
}

@Service
class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, String> userDatabase = new HashMap<>();

    public boolean isEmailRegistered(String email) {
        return userDatabase.containsKey(email);
    }

    public boolean isPasswordStrong(String password) {
        return password.length() >= 8; // Simplified strength check
    }

    public void registerUser(UserRegistrationRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        userDatabase.put(request.getEmail(), encryptedPassword);
    }
}