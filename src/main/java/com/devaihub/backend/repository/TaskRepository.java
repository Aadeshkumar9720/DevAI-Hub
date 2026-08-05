package com.devaihub.backend.repository;

import com.devaihub.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devaihub.backend.entity.TaskStatus;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);
    long countByProjectOwnerUsername(String username);

    long countByProjectOwnerUsernameAndStatus(
            String username,
            TaskStatus status
    );
}