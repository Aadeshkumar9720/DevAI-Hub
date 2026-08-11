package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.Task;
import com.devaihub.backend.response.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());

        if (task.getProject() != null) {
            response.setProjectId(task.getProject().getId());
        }

        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
}