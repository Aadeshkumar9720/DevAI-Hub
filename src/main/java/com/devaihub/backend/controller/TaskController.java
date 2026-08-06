package com.devaihub.backend.controller;

import com.devaihub.backend.dto.CreateTaskRequest;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.TaskResponse;
import com.devaihub.backend.service.interfaces.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.devaihub.backend.dto.UpdateTaskRequest;
@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@Tag(
        name = "Task Management",
        description = "Create and manage project tasks"
)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Create Task",
            description = "Creates a new task inside a project."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Task created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
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
    @Operation(
            summary = "Get All Tasks",
            description = "Returns all tasks for the specified project."
    )
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

    @Operation(
            summary = "Get Task",
            description = "Returns task details."
    )
    @GetMapping("{taskId}")
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
    @Operation(
            summary = "Update Task",
            description = "Updates an existing task."
    )
    @PutMapping("{taskId}")
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
    @Operation(
            summary = "Delete Task",
            description = "Deletes a task."
    )
    @DeleteMapping("{taskId}")
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
