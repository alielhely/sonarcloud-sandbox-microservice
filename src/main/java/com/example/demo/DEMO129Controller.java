package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Optional;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Password confirmation does not match");
        }

        if (userService.emailExists(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        userService.registerUser(registrationDto);
        return ResponseEntity.ok("Registration successful. Confirmation email sent.");
    }
}

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userRepository.save(user);
        emailService.sendConfirmationEmail(user.getEmail());
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}

interface UserRepository {
    Optional<User> findByEmail(String email);

    void save(User user);
}

class User {
    private String email;
    private String password;

    // Getters and Setters
}

class UserRegistrationDto {

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Password confirmation is required")
    private String confirmPassword;

    // Getters and Setters
}

@Service
class EmailService {
    public void sendConfirmationEmail(String email) {
        // Logic to send confirmation email
    }
}