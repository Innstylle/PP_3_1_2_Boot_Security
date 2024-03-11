package ru.kata.spring.boot_security.demo.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;
    private Collection<Long> roleIds;

}
