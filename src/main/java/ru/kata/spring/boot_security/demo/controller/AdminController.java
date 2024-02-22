package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin")
    public String showUsers(Model model) {
        List<User> userList = userService.getAllUser();
        model.addAttribute("all_user", userList);
        return "all-users";
    }

    @GetMapping("/admin/addNewUser")
    public String addNewUser(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roles);
        return "user-info";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/updateInfo")
    public String updateUser(@ModelAttribute("userId") Long id, Model model) {
        List<Role> roles = roleRepository.findAll();
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        return "user-info";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
