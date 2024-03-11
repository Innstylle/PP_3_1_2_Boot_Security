package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

//    private final UserService userService;
//    private final List<Role> roles;
//
//
//    @Autowired
//    public AdminController(UserService userService) {
//        this.userService = userService;
//        this.roles = userService.getAllRoles();
//    }

    @GetMapping("/") //admin/panel
    public String showUsers(/*Model model, Authentication authentication*/) {
//        List<User> userList = userService.getAllUser();
//        model.addAttribute("all_user", userList);
//        model.addAttribute("roles", roles);
//        myModel(model, authentication);
        return "adminPanel";
    }
//
//
//    @GetMapping("/admin/addNewUser")
//    public String addNewUser(Model model, Authentication authentication) {
//        myModel(model, authentication);
//        User user = new User();
//        model.addAttribute("userName", authentication.getName());
//        model.addAttribute("user", user);
//        model.addAttribute("allRoles", roles);
//        return "editUser";
//    }
//
//
//    @PostMapping("/admin/saveUser")
//    public String saveUser(@ModelAttribute("user") User user) {
//        userService.saveUser(user);
//        return "redirect:/admin/panel";
//    }
//
//
//    @PostMapping("/admin/editUser")
//    public String updateUser(@ModelAttribute("userId") Long id, Model model) {
//        User user = userService.getUserById(id);
//        model.addAttribute("user", user);
//        model.addAttribute("allRoles", roles);
//        return "editUser";
//    }
//
//    @PostMapping("/admin/deleteUser")
//    public String deleteUser(@RequestParam("id") Long id) {
//        userService.deleteUser(id);
//        return "redirect:/admin/panel";
//    }
//
//    protected static void myModel(Model model, Authentication authentication) {
//        model.addAttribute("activeWindowName", "Admin panel");
//        model.addAttribute("userRole", authentication != null ?
//                authentication.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList()) :
//                Collections.emptyList());
//        model.addAttribute("userName", authentication != null ? authentication.getName() : "");
//    }
}
