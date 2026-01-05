package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.regex.Pattern;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/register")
class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String WEAK_PASSWORD_REGEX = "^.{0,7}$";
    private static final Pattern WEAK_PASSWORD_PATTERN = Pattern.compile(WEAK_PASSWORD_REGEX);

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        if (WEAK_PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            return ResponseEntity.badRequest().body("Password is too weak");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        userRepository.save(user);

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

class UserRegistrationRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
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

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

@Configuration
class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}