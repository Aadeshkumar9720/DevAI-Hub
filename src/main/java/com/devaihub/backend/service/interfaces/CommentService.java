package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.dto.CreateCommentRequest;
import com.devaihub.backend.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(
            Long taskId,
            CreateCommentRequest request,
            String username
    );

    List<CommentResponse> getCommentsByTask(Long taskId);
}
