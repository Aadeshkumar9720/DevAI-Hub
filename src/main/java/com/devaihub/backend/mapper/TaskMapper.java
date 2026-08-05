package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.Task;
import com.devaihub.backend.response.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ProjectMapper projectMapper;

    public TaskMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public TaskResponse toResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setProject(projectMapper.toResponse(task.getProject()));

        return response;
    }
}