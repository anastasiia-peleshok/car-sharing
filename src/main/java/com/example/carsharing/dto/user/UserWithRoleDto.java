package com.example.carsharing.dto.user;

import com.example.carsharing.model.User;

import java.util.UUID;

public record UserWithRoleDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        User.Role role) {
}
