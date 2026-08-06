package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.response.NotificationResponse;

public interface NotificationService {

    void sendNotification(NotificationResponse notification);
}
