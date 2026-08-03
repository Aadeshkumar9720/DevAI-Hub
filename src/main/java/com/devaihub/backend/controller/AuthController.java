package com.devaihub.backend.controller;
import com.devaihub.backend.dto.RegisterRequest;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.devaihub.backend.dto.LoginRequest;
import com.devaihub.backend.response.LoginResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequest request) {

        User user = userService.registerUser(request);

        return new ApiResponse(
                true,
                "User registered successfully",
                user
        );
    }

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);

        return new ApiResponse(
                true,
                "Login successful",
                response
        );
    }
}
