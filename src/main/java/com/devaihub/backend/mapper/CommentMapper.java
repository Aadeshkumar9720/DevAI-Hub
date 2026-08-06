package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.Comment;
import com.devaihub.backend.response.CommentResponse;
import com.devaihub.backend.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {

        CommentResponse response = new CommentResponse();

        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());

        UserSummaryResponse author = new UserSummaryResponse();

        author.setId(comment.getAuthor().getId());
        author.setUsername(comment.getAuthor().getUsername());
        author.setFirstName(comment.getAuthor().getFirstName());
        author.setLastName(comment.getAuthor().getLastName());
        author.setRole(comment.getAuthor().getRole().name());

        response.setAuthor(author);

        return response;
    }
}