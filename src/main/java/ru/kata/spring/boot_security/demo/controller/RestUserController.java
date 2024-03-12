package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@RestController
@RequestMapping("/user")
public class RestUserController {


    private final UserService userService;

    public RestUserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public ResponseEntity<User> homeForUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        return ResponseEntity.ok(user);
    }
}
