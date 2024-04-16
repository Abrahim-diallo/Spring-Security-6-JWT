package fr.springsecurityjwtapp.spring_jwt_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class ContentController {

    @GetMapping("/home")
    public String handleWelcome() {
        return "welcome to REST API JWT SPRING SECURITY";
    }
}
