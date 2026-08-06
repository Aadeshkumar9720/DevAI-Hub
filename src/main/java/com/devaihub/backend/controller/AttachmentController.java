package com.devaihub.backend.controller;

import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.AttachmentResponse;
import com.devaihub.backend.service.interfaces.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/attachments")
@Tag(
        name = "Attachments",
        description = "Upload and manage task attachments"
)
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    @Operation(
            summary = "Upload Attachment",
            description = "Uploads a file to a task."
    )
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

    @Operation(
            summary = "Get Attachments",
            description = "Returns all attachments of a task."
    )
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

    @Operation(
            summary = "Delete Attachment",
            description = "Deletes an attachment."
    )
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
