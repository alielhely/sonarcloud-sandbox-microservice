package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
@RequestMapping("/register")
class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public Map<String, String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        Map<String, String> response = new HashMap<>();

        if (userService.emailExists(request.getEmail())) {
            response.put("error", "Email already exists");
            return response;
        }

        if (!isValidPassword(request.getPassword())) {
            response.put("error", "Weak password");
            return response;
        }

        userService.registerUser(request);

        sendConfirmationEmail(request.getEmail());

        response.put("message", "User registered successfully");
        return response;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private void sendConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registration Confirmation");
        message.setText("Thank you for registering!");
        mailSender.send(message);
    }
}

class UserRegistrationRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // Getters and Setters
}

@Service
class UserService {

    private final Map<String, String> userDatabase = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean emailExists(String email) {
        return userDatabase.containsKey(email);
    }

    public void registerUser(UserRegistrationRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        userDatabase.put(request.getEmail(), encryptedPassword);
    }
}