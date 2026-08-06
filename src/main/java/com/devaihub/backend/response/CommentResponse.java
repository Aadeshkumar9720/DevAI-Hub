package com.devaihub.backend.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private Long id;

    private String content;

    private UserSummaryResponse author;

    private LocalDateTime createdAt;
}
