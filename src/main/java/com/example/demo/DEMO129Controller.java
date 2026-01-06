package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

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
        if (userService.emailExists(request.getEmail())) {
            return "Error: Duplicate email registration";
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Error: Password mismatch";
        }
        userService.registerUser(request);
        return "User account created. Confirmation email sent.";
    }
}

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void registerUser(UserRegistrationRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        userRepository.save(user);
        // Logic to send confirmation email
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

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Password confirmation is mandatory")
    private String confirmPassword;

    // Getters and setters
}