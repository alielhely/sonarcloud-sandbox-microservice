package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/register")
@Validated
class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserRegistrationRequest request, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessages);
        }

        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        String encryptedPassword = userService.encryptPassword(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        userService.save(user);

        sendConfirmationEmail(request.getEmail());

        return ResponseEntity.ok("User registered successfully");
    }

    private void sendConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registration Confirmation");
        message.setText("Thank you for registering!");
        mailSender.send(message);
    }
}

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}

interface UserRepository {
    Optional<User> findByEmail(String email);
    void save(User user);
}

class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
}

class UserRegistrationRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    // Getters and setters
}