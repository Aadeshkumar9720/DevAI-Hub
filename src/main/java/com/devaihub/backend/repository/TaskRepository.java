package com.devaihub.backend.repository;

import com.devaihub.backend.entity.Task;
import com.devaihub.backend.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    long countByProjectOwnerUsername(String username);

    long countByProjectOwnerUsernameAndStatus(
            String username,
            TaskStatus status
    );
}