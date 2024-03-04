package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    List<Role> getAllRoles();

    List<User> getAllUser();

    void saveUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String name);

    void deleteUser(Long id);
}
