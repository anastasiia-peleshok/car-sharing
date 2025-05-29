package com.example.carsharing.supplier;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserRegistrationRequestDto;
import com.example.carsharing.dto.user.UserUpdateRequestDto;
import com.example.carsharing.dto.user.UserWithRoleDto;
import com.example.carsharing.model.User;

import java.util.UUID;

public class UserSupplier {
    public static UserDto getUserDto() {
        return new UserDto(
                UUID.randomUUID(),
                "john.doe@example.com",
                "John",
                "Doe"
        );
    }

    public static UserWithRoleDto getUserWithRoleDtoWithId1() {
        return new UserWithRoleDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                "+38097878338",
                "john.doe@example.com",
                User.Role.MANAGER
        );
    }

    public static User getUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(User.Role.CUSTOMER);
        user.setId( UUID.randomUUID());
        user.setPassword("encodedPassword");
        return user;
    }

    public static User getUserAlice() {
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Doe");
        user.setEmail("alice.doe@example.com");
        user.setRole(User.Role.CUSTOMER);
        user.setId( UUID.randomUUID());
        user.setPassword("encodedPassword");
        return user;
    }

    public static UserRegistrationRequestDto getUserRegistrationRequestDto() {
        return new UserRegistrationRequestDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "+380975829979",
                "password",
                "password"
        );
    }

    public static UserUpdateRequestDto getAliceUserUpdateRequestDto() {
        return new UserUpdateRequestDto(
                "Alice",
                "Doe",
                "alice.doe@example.com",
                "+380975829979",
                "password",
                "password"
        );
    }

    public static UserDto getAliceUserDto() {
        return new UserDto(
                UUID.randomUUID(),
                "alice.doe@example.com",
                "Alice",
                "Doe"
        );
    }
}
