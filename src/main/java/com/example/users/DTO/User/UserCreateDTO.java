package com.example.users.DTO.User;

import java.util.List;

public record UserCreateDTO (
        String email,
        String password,
        List<String> roles,
        String name,
        String phoneNumber
) {}
