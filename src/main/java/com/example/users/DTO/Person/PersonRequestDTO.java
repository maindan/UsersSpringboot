package com.example.users.DTO.Person;

import com.example.users.DTO.User.UserRequestDTO;
import com.example.users.model.User;

public record PersonRequestDTO(
        Long id,
        String name,
        String phoneNumber,
        UserRequestDTO user
) {
}
