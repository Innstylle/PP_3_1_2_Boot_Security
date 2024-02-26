package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.stream.Collectors;

@Controller
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("showProfileButton", true);
        }
        return "index";
    }

    @GetMapping("/userInfoPanel")
    public String homeForUser(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            User user = userService.getUserByName(username);
            model.addAttribute("activeWindowName","User panel");
            model.addAttribute("userRole",authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            model.addAttribute("userName", authentication.getName());
            model.addAttribute("content", "user_content");
            model.addAttribute("user", user);
            return "userInfoPanel";
        } else {
            return "index";
        }
    }
}
