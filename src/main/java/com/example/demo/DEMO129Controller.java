package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
@RequestMapping("/register")
class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUser(@Validated @RequestBody UserRegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Error: Password mismatch";
        }
        if (userService.isEmailRegistered(request.getEmail())) {
            return "Error: Email is already in use";
        }
        userService.registerUser(request);
        return "Registration successful, confirmation email sent";
    }
}

class UserRegistrationRequest {

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    private String confirmPassword;

    // Getters and Setters
}

@Service
class UserService {

    private final Map<String, String> userDatabase = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JavaMailSender mailSender;

    public boolean isEmailRegistered(String email) {
        return userDatabase.containsKey(email);
    }

    public void registerUser(UserRegistrationRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userDatabase.put(request.getEmail(), encodedPassword);
        sendConfirmationEmail(request.getEmail());
    }

    private void sendConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registration Confirmation");
        message.setText("Thank you for registering!");
        mailSender.send(message);
    }
}