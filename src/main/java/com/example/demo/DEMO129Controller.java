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
@RequestMapping("/register")
class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUser(@Validated @RequestBody UserRegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Error: Passwords do not match.";
        }

        Optional<User> existingUser = userService.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "Error: Email is already registered.";
        }

        userService.registerUser(request);
        return "Registration successful!";
    }
}

@Service
class UserService {

    private final Map<String, User> userDatabase = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userDatabase.get(email));
    }

    public void registerUser(UserRegistrationRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        userDatabase.put(user.getEmail(), user);
    }
}

class User {

    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

class UserRegistrationRequest {

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}