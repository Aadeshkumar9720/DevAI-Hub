package com.devaihub.backend.controller;
import com.devaihub.backend.dto.RegisterRequest;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.devaihub.backend.dto.LoginRequest;
import com.devaihub.backend.response.LoginResponse;
import com.devaihub.backend.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Authentication",
        description = "User registration and login APIs"
)
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @Operation(
            summary = "Register User",
            description = "Creates a new user account."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Username or email already exists"
            )
    })
    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequest request) {

        User user = userService.registerUser(request);

        return new ApiResponse(
                true,
                "User registered successfully",
                user
        );
    }
    @Operation(
            summary = "User Login",
            description = "Authenticates the user and returns a JWT token."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password"
            )
    })
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
