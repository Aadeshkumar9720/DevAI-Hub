package com.devaihub.backend.repository;

import com.devaihub.backend.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    @Query("""
        SELECT a
        FROM Activity a
        JOIN FETCH a.performedBy
        WHERE a.project.id = :projectId
        ORDER BY a.createdAt DESC
    """)
    List<Activity> findByProjectIdWithPerformedBy(
            @Param("projectId") Long projectId
    );
}
