package com.devaihub.backend.repository;

import com.devaihub.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserUsernameOrderByCreatedAtDesc(
            String username
    );
    long countByUserUsernameAndReadFalse(String username);
}