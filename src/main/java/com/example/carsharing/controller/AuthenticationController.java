package com.example.carsharing.controller;

import com.example.carsharing.dto.user.UserDto;
import com.example.carsharing.dto.user.UserLoginRequestDto;
import com.example.carsharing.dto.user.UserLoginResponseDto;
import com.example.carsharing.dto.user.UserRegistrationRequestDto;
import com.example.carsharing.security.AuthenticationService;
import com.example.carsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register user", description = "Create an account")
    @PostMapping("/registration")
    public UserDto register(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        return userService.save(userRegistrationRequestDto);
    }

    @Operation(summary = "Login user", description = "Login to your account")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
