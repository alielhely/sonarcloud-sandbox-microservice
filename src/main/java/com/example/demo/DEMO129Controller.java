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
import java.util.Optional;

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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public Map<String, String> registerUser(@Valid @RequestBody RegistrationRequest request) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.put("message", "Error: Email is already registered.");
            return response;
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("message", "Error: Passwords do not match.");
            return response;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        response.put("message", "Registration successful.");
        return response;
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

class RegistrationRequest {
    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters.")
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @NotBlank(message = "Confirm Password is mandatory.")
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