package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.CreateTaskRequest;
import com.devaihub.backend.dto.UpdateTaskRequest;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.entity.Task;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.mapper.TaskMapper;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.TaskRepository;
import com.devaihub.backend.response.TaskResponse;
import com.devaihub.backend.service.interfaces.ActivityService;
import com.devaihub.backend.service.interfaces.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
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
            TaskMapper taskMapper, ActivityService activityService,NotificationService notificationService
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
        this.activityService = activityService;
        this.notificationService = notificationService;
    }

    @Override
    public TaskResponse createTask(
            Long projectId,
            CreateTaskRequest request,
            String username
    ) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        if (!project.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to add tasks");
        }

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        activityService.logActivity(
                project,
                project.getOwner(),
                ActivityType.TASK_CREATED,
                "Task '" + savedTask.getTitle() + "' was created."
        );
        notificationService.sendNotification(
                new NotificationResponse(
                        "New Task",
                        "Task '" + savedTask.getTitle() + "' has been created.",
                        "TASK_CREATED"
                )
        );
        return taskMapper.toResponse(savedTask);
    }
    @Override
    public List<TaskResponse> getTasksByProject(Long projectId) {

        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse updateTask(
            Long taskId,
            UpdateTaskRequest request,
            String username) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        if (!task.getProject().getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to update this task");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);
        activityService.logActivity(
                task.getProject(),
                task.getProject().getOwner(),
                ActivityType.TASK_UPDATED,
                "Task '" + updatedTask.getTitle() + "' was updated."
        );
        activityService.logActivity(
                task.getProject(),
                task.getProject().getOwner(),
                ActivityType.TASK_UPDATED,
                "Task '" + updatedTask.getTitle() + "' was updated."
        );
        return taskMapper.toResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId, String username) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        if (!task.getProject().getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to delete this task");
        }

        taskRepository.delete(task);
    }

}
