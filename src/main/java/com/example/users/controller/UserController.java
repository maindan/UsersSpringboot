package com.example.users.controller;

import com.example.users.DTO.User.UserCreateDTO;
import com.example.users.model.User;
import com.example.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public User create(@RequestBody UserCreateDTO userData) throws Exception {
        return userService.createUser(userData);
    }
}
