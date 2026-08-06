package com.devaihub.backend.service.impl;

import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(NotificationResponse notification) {

        messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
        );
    }
}
