package com.example.carsharing.controller;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserUpdateRequestDto;
import com.example.carsharing.model.User;
import com.example.carsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Get user by ID.
     */
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique identifier.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    /**
     * Get user by email.
     */
    @Operation(summary = "Get user by email", description = "Retrieve a user by their email address.")
    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Update user information (accessible by MANAGER).
     */
    @Operation(summary = "Update user", description = "Update basic user information. Requires MANAGER role.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MANAGER')")
    public UserDto updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.updateUser(id, userUpdateRequestDto);
    }

    /**
     * Update user role (accessible by MANAGER).
     */
    @Operation(summary = "Update user role", description = "Change the role of a user. Requires MANAGER role.")
    @PutMapping("/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MANAGER')")
    public UserDto updateRole(
            @PathVariable UUID id,
            @RequestParam User.Role role) {
        return userService.updateRole(id, role);
    }

    /**
     * Delete user by ID.
     */
    @Operation(summary = "Delete user", description = "Remove a user from the system by ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
