package com.devaihub.backend.controller;

import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.AttachmentResponse;
import com.devaihub.backend.service.interfaces.AttachmentService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(
            AttachmentService attachmentService
    ) {
        this.attachmentService = attachmentService;
    }

    // =========================================================
    // OPEN ATTACHMENT IN BROWSER
    // =========================================================

    @GetMapping("/{attachmentId}/open")
    public ResponseEntity<Resource> openAttachment(
            @PathVariable Long attachmentId
    ) {

        AttachmentResponse attachment =
                attachmentService.getAttachment(
                        attachmentId
                );

        Resource resource =
                attachmentService.getAttachmentFile(
                        attachmentId
                );

        MediaType mediaType =
                getMediaType(
                        attachment.getFileType()
                );

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                attachment.getFileName() +
                                "\""
                )
                .body(resource);
    }

    // =========================================================
    // DOWNLOAD ATTACHMENT
    // =========================================================

    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long attachmentId
    ) {

        AttachmentResponse attachment =
                attachmentService.getAttachment(
                        attachmentId
                );

        Resource resource =
                attachmentService.getAttachmentFile(
                        attachmentId
                );

        MediaType mediaType =
                getMediaType(
                        attachment.getFileType()
                );

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                attachment.getFileName() +
                                "\""
                )
                .body(resource);
    }

    // =========================================================
    // MEDIA TYPE
    // =========================================================

    private MediaType getMediaType(
            String fileType
    ) {

        if (fileType == null ||
                fileType.isBlank()) {

            return MediaType.APPLICATION_OCTET_STREAM;
        }

        try {
            return MediaType.parseMediaType(
                    fileType
            );
        } catch (Exception e) {

            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
