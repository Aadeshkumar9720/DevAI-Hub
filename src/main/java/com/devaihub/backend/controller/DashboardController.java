package com.devaihub.backend.controller;

import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.DashboardStatsResponse;
import com.devaihub.backend.service.interfaces.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(
        name = "Dashboard",
        description = "Dashboard statistics APIs"
)
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(
            summary = "Dashboard Statistics",
            description = "Returns dashboard statistics for the authenticated user."
    )
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
