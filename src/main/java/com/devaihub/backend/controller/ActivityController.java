package com.devaihub.backend.controller;

import com.devaihub.backend.response.ActivityResponse;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.service.interfaces.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityResponse>>> getActivities(
            @PathVariable Long projectId
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Activities fetched successfully",
                        activityService.getProjectActivities(projectId)
                )
        );
    }
}
