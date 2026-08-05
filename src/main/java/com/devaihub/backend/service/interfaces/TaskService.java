package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.dto.CreateTaskRequest;
import com.devaihub.backend.response.TaskResponse;
import java.util.List;

import com.devaihub.backend.dto.UpdateTaskRequest;
public interface TaskService {

    TaskResponse createTask(
            Long projectId,
            CreateTaskRequest request,
            String username
    );
    List<TaskResponse> getTasksByProject(Long projectId);

    TaskResponse getTaskById(Long taskId);

    TaskResponse updateTask(
            Long taskId,
            UpdateTaskRequest request,
            String username
    );

    void deleteTask(Long taskId, String username);
}
