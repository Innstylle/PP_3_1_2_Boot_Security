package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    Role getRoleById(Long roleId);

    List<Role> getAllRoles();

    List<Role> getRolesByIds(List<Long> roleIds);

    List<User> getAllUser();

    void saveUser(User user, List<Role> roleIds);

    User getUserById(Long id);

    User getUserByEmail(String name);

    void deleteUser(Long id);

}
