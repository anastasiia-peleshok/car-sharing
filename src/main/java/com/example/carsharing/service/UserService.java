package com.example.carsharing.service;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserRegistrationRequestDto;
import com.example.carsharing.dto.user.UserUpdateRequestDto;
import com.example.carsharing.model.User;

import java.util.UUID;

public interface UserService {
    UserDto save(UserRegistrationRequestDto requestDto);

    UserDto getUserById(UUID id);

    UserDto getUserByEmail(String email);

    UserDto updateRole(UUID userToUpdateId, User.Role role);

    void deleteUser(UUID id);

    UserDto updateUser(UUID userToUpdateId, UserUpdateRequestDto userDto);
}
