package com.example.carsharing.service.impl;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserRegistrationRequestDto;
import com.example.carsharing.dto.user.UserUpdateRequestDto;
import com.example.carsharing.mapper.UserMapper;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto save(UserRegistrationRequestDto requestDto) {
        checkEmailIsFree(requestDto.email());
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("There is no user with id: " + id));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("There is no user with email: " + email));
    }

    @Override
    public UserDto updateUser(UUID userToUpdateId, UserUpdateRequestDto userDto) {
        User userToUpdate = userRepository.findById(userToUpdateId)
                .orElseThrow(() -> new IllegalArgumentException("There is no user with id: " + userToUpdateId));
        if (!userDto.email().equals(userToUpdate.getEmail())) {
            checkEmailIsFree(userDto.email());
        }
        userMapper.updateUserFromDto(userDto, userToUpdate);
        return userMapper.toDto(userRepository.save(userToUpdate));
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no user with id: " + id));
        userRepository.deleteById(id);
    }

    private void checkEmailIsFree(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " is already exist.");
        }
    }
}
