package com.devaihub.backend.controller;

import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.AttachmentResponse;
import com.devaihub.backend.service.interfaces.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskAttachmentController {

    private final AttachmentService attachmentService;

    public TaskAttachmentController(
            AttachmentService attachmentService
    ) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/{taskId}/attachments")
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> getAttachments(
            @PathVariable Long taskId
    ) {

        List<AttachmentResponse> attachments =
                attachmentService.getAttachments(taskId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Attachments fetched successfully",
                        attachments
                )
        );
    }

    @PostMapping("/{taskId}/attachments")
    public ResponseEntity<ApiResponse<AttachmentResponse>> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        AttachmentResponse attachment =
                attachmentService.uploadFile(
                        taskId,
                        file,
                        authentication.getName()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "File uploaded successfully",
                        attachment
                )
        );
    }

    @DeleteMapping("/{taskId}/attachments/{attachmentId}")
    public ResponseEntity<ApiResponse<String>> deleteAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId,
            Authentication authentication
    ) {

        attachmentService.deleteAttachment(
                attachmentId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Attachment deleted successfully",
                        null
                )
        );
    }
}