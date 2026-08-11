package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.CreateTaskRequest;
import com.devaihub.backend.dto.UpdateTaskRequest;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.entity.Task;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.mapper.TaskMapper;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.TaskRepository;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.response.TaskResponse;
import com.devaihub.backend.service.interfaces.ActivityService;
import com.devaihub.backend.service.interfaces.NotificationService;
import com.devaihub.backend.service.interfaces.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final ActivityService activityService;
    private final NotificationService notificationService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            TaskMapper taskMapper,
            ActivityService activityService,
            NotificationService notificationService
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
        this.activityService = activityService;
        this.notificationService = notificationService;
    }

    // =========================================================
    // CREATE TASK
    // =========================================================

    @Override
    @Transactional
    public TaskResponse createTask(
            Long projectId,
            CreateTaskRequest request,
            String username
    ) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found")
                );

        // Check project owner
        if (project.getOwner() == null ||
                !project.getOwner()
                        .getUsername()
                        .equals(username)) {

            throw new RuntimeException(
                    "You are not allowed to add tasks"
            );
        }

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setProject(project);

        Task savedTask = taskRepository.save(task);

        // =====================================================
        // ACTIVITY
        // =====================================================

        activityService.logActivity(
                project,
                project.getOwner(),
                ActivityType.TASK_CREATED,
                "Task '" + savedTask.getTitle() +
                        "' was created."
        );

        // =====================================================
        // NOTIFICATION
        // =====================================================

        notificationService.sendNotification(
                new NotificationResponse(
                        "New Task",
                        "Task '" + savedTask.getTitle() +
                                "' has been created.",
                        "TASK_CREATED"
                ),
                project.getOwner().getUsername()
        );

        return taskMapper.toResponse(savedTask);
    }

    // =========================================================
    // GET ALL TASKS FOR PROJECT
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByProject(
            Long projectId
    ) {

        return taskRepository
                .findByProjectId(projectId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // =========================================================
    // GET TASK BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(
            Long taskId
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );

        return taskMapper.toResponse(task);
    }

    // =========================================================
    // UPDATE TASK
    // =========================================================

    @Override
    @Transactional
    public TaskResponse updateTask(
            Long taskId,
            UpdateTaskRequest request,
            String username
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );

        Project project = task.getProject();

        if (project == null) {
            throw new RuntimeException(
                    "Task is not associated with a project"
            );
        }

        // Check project owner
        if (project.getOwner() == null ||
                !project.getOwner()
                        .getUsername()
                        .equals(username)) {

            throw new RuntimeException(
                    "You are not allowed to update this task"
            );
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);

        // =====================================================
        // ACTIVITY
        // =====================================================

        activityService.logActivity(
                project,
                project.getOwner(),
                ActivityType.TASK_UPDATED,
                "Task '" + updatedTask.getTitle() +
                        "' was updated."
        );

        return taskMapper.toResponse(updatedTask);
    }

    // =========================================================
    // DELETE TASK
    // =========================================================

    @Override
    @Transactional
    public void deleteTask(
            Long taskId,
            String username
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );

        Project project = task.getProject();

        if (project == null) {
            throw new RuntimeException(
                    "Task is not associated with a project"
            );
        }

        // Check project owner
        if (project.getOwner() == null ||
                !project.getOwner()
                        .getUsername()
                        .equals(username)) {

            throw new RuntimeException(
                    "You are not allowed to delete this task"
            );
        }

        String taskTitle = task.getTitle();

        taskRepository.delete(task);

        // =====================================================
        // ACTIVITY
        // =====================================================

        activityService.logActivity(
                project,
                project.getOwner(),
                ActivityType.TASK_DELETED,
                "Task '" + taskTitle +
                        "' was deleted."
        );
    }
}
