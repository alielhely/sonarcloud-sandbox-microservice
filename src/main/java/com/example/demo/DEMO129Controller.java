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

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String LOGIN_VIEW = "login";

    @GetMapping(LOGIN_ENDPOINT)
    public String login() {
        return LOGIN_VIEW;
    }
}