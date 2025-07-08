package com.example.carsharing.service.impl;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserRegistrationRequestDto;
import com.example.carsharing.dto.user.UserUpdateRequestDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.exceptions.RegistrationException;
import com.example.carsharing.mapper.UserMapper;
import com.example.carsharing.model.NotificationSubject;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.NotificationService;
import com.example.carsharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;


    @Override
    public UserDto save(UserRegistrationRequestDto requestDto) {
        ensureEmailIsAvailable(requestDto.email());
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        User savedUser = userRepository.save(user);
        notificationService.sendNotification(savedUser, NotificationSubject.USER_REGISTRATION.toString(), "Dear "
                + savedUser.getFirstName() + " " + savedUser.getLastName() + " you have successfully registered on CarSharing.com");
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        return userMapper.toDto(getUser(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with email: " + email));
    }

    @Override
    public UserDto updateUser(UUID userToUpdateId, UserUpdateRequestDto userDto) {
        User userToUpdate = getUser(userToUpdateId);
        if (userDto.email() != null && !userDto.email().equals(userToUpdate.getEmail())) {
            ensureEmailIsAvailable(userDto.email());
        }
        userMapper.updateUserFromDto(userDto, userToUpdate);
        return userMapper.toDto(userToUpdate);
    }

    private User getUser(UUID userToUpdateId) {
        return userRepository.findById(userToUpdateId)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id: " + userToUpdateId));
    }

    @Override
    public UserDto updateRole(UUID userToUpdateId, User.Role role) {
        User userToUpdate = getUser(userToUpdateId);
        userToUpdate.setRole(role);
        return userMapper.toDto(userToUpdate);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    private void ensureEmailIsAvailable(String email) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new RegistrationException("A user with email " + email + " already exists.");
        }
    }
}
