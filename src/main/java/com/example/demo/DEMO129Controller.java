package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    @PostMapping
    public String registerUser(@RequestBody UserRegistrationRequest request) {
        if (!isValidEmail(request.getEmail())) {
            return "Error: Invalid email address";
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Error: Passwords do not match";
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Error: Email address already registered";
        }
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        userRepository.save(user);
        sendConfirmationEmail(request.getEmail());
        return "User registered successfully";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void sendConfirmationEmail(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Registration Confirmation");
            message.setText("Thank you for registering!");
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}

interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
}

class UserRegistrationRequest {
    private String email;
    private String password;
    private String confirmPassword;

    // Getters and setters
}