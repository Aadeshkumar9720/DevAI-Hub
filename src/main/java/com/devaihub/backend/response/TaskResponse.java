package com.devaihub.backend.response;

import com.devaihub.backend.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private Long projectId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
