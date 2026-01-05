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

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Map<String, String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        Map<String, String> response = new HashMap<>();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("message", "Password mismatch");
            return response;
        }

        if (userService.existsByEmail(request.getEmail())) {
            response.put("message", "Duplicate email");
            return response;
        }

        userService.registerUser(request);
        response.put("message", "User registered successfully");
        return response;
    }
}

class UserRegistrationRequest {

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;

    // Getters and Setters
}

@Service
class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        // Send confirmation email logic here
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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

    // Getters and Setters
}

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}