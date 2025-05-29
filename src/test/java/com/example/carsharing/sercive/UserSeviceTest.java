package com.example.carsharing.sercive;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserRegistrationRequestDto;
import com.example.carsharing.dto.user.UserUpdateRequestDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.exceptions.RegistrationException;
import com.example.carsharing.mapper.UserMapper;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.impl.UserServiceImpl;
import com.example.carsharing.supplier.UserSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSeviceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidRegistrationRequestDto_ReturnsUserDto() {
        UserRegistrationRequestDto requestDto = UserSupplier.getUserRegistrationRequestDto();
        User user = UserSupplier.getUser();
        UserDto expected = UserSupplier.getUserDto();

        when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn(user.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);

        UserDto actual = userService.save(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(requestDto.email());
        verify(userMapper, times(1)).toModel(requestDto);
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder);
    }
    @Test
    @DisplayName("Verify save() method throws exception when email is already taken")
    public void save_EmailTaken_ThrowsException() {
        UserRegistrationRequestDto requestDto = UserSupplier.getUserRegistrationRequestDto();
        User user = UserSupplier.getUser();
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        RegistrationException exception = assertThrows(
                RegistrationException.class,
                () -> userService.save(requestDto)
        );

        String expected = "User with email " + email + " is already exist.";
        String actual = exception.getMessage();

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder);
    }

    @Test
    @DisplayName("Verify the correct user was returned when user exists")
    public void findById_ValidId_ReturnsUserDto() {
        UUID userId = UUID.randomUUID();
        User user = UserSupplier.getUser();
        UserDto expected = UserSupplier.getUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expected);

        UserDto actual = userService.getUserById(userId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Verify the exception was thrown when invalid id")
    public void findById_InvalidId_ThrowsException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        String expected = "There is no user with id: " + userId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Verify update() method works")
    public void update_ValidRequestDto_ReturnsUserDto() {
        UserUpdateRequestDto requestDto = UserSupplier.getAliceUserUpdateRequestDto();
        User user = UserSupplier.getUserAlice();
        UserDto expected = UserSupplier.getAliceUserDto();

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expected);

        UserDto actual = userService.updateUser(user.getId(), requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userMapper, times(1)).updateUserFromDto(requestDto, user);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Verify save() method throws exception when email is already taken")
    public void update_EmailTaken_ThrowsException() {
        UserUpdateRequestDto requestDto = UserSupplier.getAliceUserUpdateRequestDto();
        User user = UserSupplier.getUser();
        String email = requestDto.email();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        RegistrationException exception = assertThrows(
                RegistrationException.class,
                () -> userService.updateUser(user.getId(), requestDto)
        );

        String expected = "User with email " + email + " is already exist.";
        String actual = exception.getMessage();

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository, userMapper);
    }
}
