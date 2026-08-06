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
@RequestMapping("/api/v1/tasks/{taskId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<AttachmentResponse>> uploadFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "File uploaded successfully",
                        attachmentService.uploadFile(
                                taskId,
                                file,
                                authentication.getName()
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> getAttachments(
            @PathVariable Long taskId
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Attachments fetched successfully",
                        attachmentService.getAttachments(taskId)
                )
        );
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<ApiResponse<String>> deleteAttachment(
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
