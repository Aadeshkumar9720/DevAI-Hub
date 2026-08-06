package com.devaihub.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/v1/test")

@Tag(
        name = "Testing",
        description = "APIs for testing backend functionality"
)
public class TestController {
    @Operation(
            summary = "Test API",
            description = "Checks whether the backend service is running and returns the authenticated user's name."
    )
    @GetMapping
    public String test(Authentication authentication) {
        return "Welcome " + authentication.getName();
    }
}