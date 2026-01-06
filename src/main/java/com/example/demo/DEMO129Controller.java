package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

@RestController
@RequestMapping("/api/register")
class RegistrationController {

    private final Map<String, String> registeredUsers = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String registerUser(@Valid @RequestBody RegistrationRequest request) {
        if (registeredUsers.containsKey(request.getEmail())) {
            return "Error: Email already registered";
        }
        if (!isValidEmail(request.getEmail())) {
            return "Error: Invalid email address";
        }
        if (!isStrongPassword(request.getPassword())) {
            return "Error: Weak password";
        }
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        registeredUsers.put(request.getEmail(), encryptedPassword);
        return "Registration successful. Confirmation message sent.";
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8;
    }
}

class RegistrationRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is mandatory")
    private String password;

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
}