package com.devaihub.backend.controller;

import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.DashboardStatsResponse;
import com.devaihub.backend.service.interfaces.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats(
            Authentication authentication) {

        DashboardStatsResponse stats =
                dashboardService.getDashboardStats(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Dashboard statistics fetched successfully",
                        stats
                )
        );
    }
}
