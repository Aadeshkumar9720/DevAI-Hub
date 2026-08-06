package com.devaihub.backend.repository;

import com.devaihub.backend.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    List<ProjectMember> findByProjectId(Long projectId);

    Optional<ProjectMember> findByProjectIdAndUserUsername(
            Long projectId,
            String username
    );

    boolean existsByProjectIdAndUserUsername(
            Long projectId,
            String username
    );
}
