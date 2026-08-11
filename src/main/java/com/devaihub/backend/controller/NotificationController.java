package com.devaihub.backend.controller;

import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(
        name = "Notifications",
        description = "Notification management APIs"
)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Get Notifications",
            description = "Returns notifications for the logged-in user."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Notifications fetched successfully",
                        notificationService.getNotifications(
                                authentication.getName()
                        )
                )
        );
    }
    @Operation(
            summary = "Mark Notification as Read",
            description = "Marks a notification as read for the logged-in user."
    )
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {

        notificationService.markAsRead(
                notificationId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Notification marked as read",
                        null
                )
        );
    }
    @Operation(
            summary = "Get Unread Notification Count",
            description = "Returns the number of unread notifications for the logged-in user."
    )
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Unread notification count fetched successfully",
                        notificationService.getUnreadCount(
                                authentication.getName()
                        )
                )
        );
    }

}
