package com.example.users.DTO.User;

public record UserCreateDTO (
        String email,
        String password,
        String name,
        String phoneNumber
) {}
