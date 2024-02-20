package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
@Repository
public interface UserDao{
    List<User> getAllUser();

    void saveUser(User user);

    User getUserById(Long id);

    User getUserByName(String name);

    void deleteUser(Long id);
}
