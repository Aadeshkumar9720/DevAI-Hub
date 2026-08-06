package com.devaihub.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Roles",
        description = "Role management APIs"
)
public class RoleController {
    @Operation(
            summary = "Admin Access",
            description = "Accessible only to users with the ADMIN role."
    )
    @GetMapping("/api/v1/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Welcome Admin!";
    }
    @Operation(
            summary = "User Access",
            description = "Accessible to users with USER or ADMIN roles."
    )

    @GetMapping("/api/v1/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String user() {
        return "Welcome User!";
    }
}
