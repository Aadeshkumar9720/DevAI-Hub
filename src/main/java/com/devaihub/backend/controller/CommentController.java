package com.devaihub.backend.controller;

import com.devaihub.backend.dto.CreateCommentRequest;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.CommentResponse;
import com.devaihub.backend.service.interfaces.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Comment added successfully",
                        commentService.createComment(
                                taskId,
                                request,
                                authentication.getName()
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByTask(
            @PathVariable Long taskId
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Comments fetched successfully",
                        commentService.getCommentsByTask(taskId)
                )
        );
    }
}
