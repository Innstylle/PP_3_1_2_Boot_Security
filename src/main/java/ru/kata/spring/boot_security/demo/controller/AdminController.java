package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String showUsers(Model model, Authentication authentication) {
        myModel(model,authentication    );
        return "adminPanel";
    }

    protected static void myModel(Model model, Authentication authentication) {
        model.addAttribute("activeWindowName", "Admin panel");
        model.addAttribute("userRole", authentication != null ?
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()) :
                Collections.emptyList());
        model.addAttribute("userName", authentication != null ? authentication.getName() : "");
    }
}
