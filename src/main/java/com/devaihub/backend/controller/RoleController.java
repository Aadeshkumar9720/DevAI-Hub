package com.devaihub.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @GetMapping("/api/v1/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Welcome Admin!";
    }

    @GetMapping("/api/v1/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String user() {
        return "Welcome User!";
    }
}
