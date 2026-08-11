package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void sendNotification(
            NotificationResponse notification,
            String username
    );

    List<NotificationResponse> getNotifications(
            String username
    );

    void markAsRead(
            Long notificationId,
            String username
    );
    long getUnreadCount(String username);
}
