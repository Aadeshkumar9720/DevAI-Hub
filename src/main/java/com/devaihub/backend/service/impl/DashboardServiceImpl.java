package com.devaihub.backend.service.impl;

import com.devaihub.backend.entity.ProjectStatus;
import com.devaihub.backend.entity.TaskStatus;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.TaskRepository;
import com.devaihub.backend.response.DashboardStatsResponse;
import com.devaihub.backend.service.interfaces.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DashboardServiceImpl(
            ProjectRepository projectRepository,
            TaskRepository taskRepository
    ) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public DashboardStatsResponse getDashboardStats(String username) {

        long totalProjects =
                projectRepository.countByOwnerUsername(username);

        long completedProjects =
                projectRepository.countByOwnerUsernameAndStatus(
                        username,
                        ProjectStatus.COMPLETED
                );

        long totalTasks =
                taskRepository.countByProjectOwnerUsername(username);

        long completedTasks =
                taskRepository.countByProjectOwnerUsernameAndStatus(
                        username,
                        TaskStatus.DONE
                );

        long todoTasks =
                taskRepository.countByProjectOwnerUsernameAndStatus(
                        username,
                        TaskStatus.TODO
                );

        long inProgressTasks =
                taskRepository.countByProjectOwnerUsernameAndStatus(
                        username,
                        TaskStatus.IN_PROGRESS
                );

        return new DashboardStatsResponse(
                totalProjects,
                completedProjects,
                totalTasks,
                completedTasks,
                todoTasks,
                inProgressTasks
        );
    }
}
