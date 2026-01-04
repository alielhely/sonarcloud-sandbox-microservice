package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Controller
@RequestMapping("/login")
class LoginController {

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        @RequestParam(required = false) boolean rememberMe, 
                        Model model, 
                        HttpSession session) {
        if (username.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Fields cannot be empty");
            return "login";
        }

        if (isValidUser(username, password)) {
            session.setAttribute("user", username);
            if (rememberMe) {
                // Logic to remember the user
            }
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    private boolean isValidUser(String username, String password) {
        // Placeholder for actual user validation logic
        return "validUser".equals(username) && "validPass".equals(password);
    }
}

@Controller
@RequestMapping("/dashboard")
class DashboardController {

    @GetMapping
    public String dashboard(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}