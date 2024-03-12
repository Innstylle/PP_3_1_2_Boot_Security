package ru.kata.spring.boot_security.demo.controller;


import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api")
public class RestAdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public RestAdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/showUsersJson")
    public ResponseEntity<List<UserDTO>> showUsers() {
        List<User> users = userService.getAllUser();
        List<UserDTO> userDTOs = users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/showUser/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userdto) {
        User user = convertToUser(userdto);
        List<Role> roles = new ArrayList<>();
        for (Long roleId : userdto.getRoleIds()) {
            Role role = userService.getRoleById(roleId);
            if (role != null) {
                roles.add(role);
            }
        }
        userService.saveUser(user, roles);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/editUser/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO updatedUser) {
        User user = convertToUser(updatedUser);
        user.setId(id);
        List<Role> roles = userService.getRolesByIds(updatedUser.getRoleIds().stream().toList());
        userService.saveUser(user, roles);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allRoles")
    public ResponseEntity<List<Role>> showRoles() {
        List<Role> roles = userService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    private User convertToUser(UserDTO userdto) {
        return modelMapper.map(userdto, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
