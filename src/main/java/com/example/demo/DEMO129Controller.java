package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
class DemoController {

    private static final String LOGIN_PATH = "/login";
    private static final String LOGIN_LITERAL = "login";
    private static final String MESSAGE_LITERAL = "message";

    @GetMapping(LOGIN_PATH)
    public String login() {
        return LOGIN_LITERAL;
    }

    @GetMapping("/message")
    public String message() {
        return MESSAGE_LITERAL;
    }
}