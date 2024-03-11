package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

    private static RoleRepository roleRepository;
    private static UserService userService;

    public SpringBootSecurityDemoApplication(RoleRepository roleRepository, UserService userService) {
        SpringBootSecurityDemoApplication.roleRepository = roleRepository;
        SpringBootSecurityDemoApplication.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }

    @PostConstruct
    public static void initialize() {
        List<Role> defaultRoles = Arrays.asList(
                new Role(Long.getLong("1"), "ROLE_ADMIN", null),
                new Role(Long.getLong("2"), "ROLE_USER", null)
        );

        for (Role role : defaultRoles) {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
            }
        }
        User defaultUser = userService.getUserByEmail("user@gmail.com");
        if (defaultUser == null) {
            defaultUser = new User(null, "user", "password",
                    "user", 10, "user@gmail.com", defaultRoles);
            userService.saveUser(defaultUser, defaultRoles);
        }
    }
}
