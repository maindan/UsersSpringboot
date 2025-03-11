package com.example.users.DTO.User;

import com.example.users.model.Role;

import java.util.List;

public record UserRequestDTO(
        Long id,
        String email,
        List<Role> roles
) {}
