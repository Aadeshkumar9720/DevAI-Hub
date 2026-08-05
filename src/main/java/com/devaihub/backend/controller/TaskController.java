package com.devaihub.backend.controller;

import com.devaihub.backend.dto.CreateTaskRequest;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.TaskResponse;
import com.devaihub.backend.service.interfaces.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.devaihub.backend.dto.UpdateTaskRequest;
@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Task created successfully",
                        taskService.createTask(
                                projectId,
                                request,
                                authentication.getName()
                        )
                )
        );
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByProject(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Tasks fetched successfully",
                        taskService.getTasksByProject(projectId)
                )
        );
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Task fetched successfully",
                        taskService.getTaskById(taskId)
                )
        );
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Task updated successfully",
                        taskService.updateTask(
                                taskId,
                                request,
                                authentication.getName()
                        )
                )
        );
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<String>> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        taskService.deleteTask(
                taskId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Task deleted successfully",
                        null
                )
        );
    }
}
