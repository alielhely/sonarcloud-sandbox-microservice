package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        if (userService.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        if (!userService.isValidEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email address");
        }

        if (!userService.isStrongPassword(request.getPassword())) {
            return ResponseEntity.badRequest().body("Password is too weak");
        }

        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully. Confirmation email sent.");
    }
}

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isValidEmail(String email) {
        return email.contains("@");
    }

    public boolean isStrongPassword(String password) {
        return password.length() >= 8;
    }

    public void registerUser(UserRegistrationRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        userRepository.save(user);
        emailService.sendConfirmationEmail(user.getEmail());
    }
}

interface UserRepository {
    Optional<User> findByEmail(String email);
    void save(User user);
}

interface EmailService {
    void sendConfirmationEmail(String email);
}

class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters omitted for brevity
}

class UserRegistrationRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    // Getters and setters omitted for brevity
}