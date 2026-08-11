package com.devaihub.backend.service.impl;

import com.devaihub.backend.entity.Notification;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.repository.NotificationRepository;
import com.devaihub.backend.repository.UserRepository;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.messaging.simp.user.SimpUserRegistry;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry simpUserRegistry;
    public NotificationServiceImpl(
            SimpMessagingTemplate messagingTemplate,
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            SimpUserRegistry simpUserRegistry
    ) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.simpUserRegistry = simpUserRegistry;
    }

    @Override
    @Transactional
    public void sendNotification(
            NotificationResponse notification,
            String username
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Notification entity = new Notification();

        entity.setTitle(notification.getTitle());
        entity.setMessage(notification.getMessage());
        entity.setType(notification.getType());
        entity.setRead(false);
        entity.setUser(user);

        Notification saved =
                notificationRepository.save(entity);
        System.out.println("====================================");
        System.out.println("WEBSOCKET SEND DEBUG");
        System.out.println("TARGET USERNAME = " + username);
        System.out.println("CONNECTED USERS:");

        simpUserRegistry.getUsers().forEach(connectedUser -> {
            System.out.println(
                    "USER = " + connectedUser.getName()
                            + " | SESSIONS = "
                            + connectedUser.getSessions().size()
            );
        });

        System.out.println("====================================");
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(String username) {

        return notificationRepository
                .findByUserUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(notification -> new NotificationResponse(
                        notification.getId(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getType(),
                        notification.isRead()
                ))
                .toList();
    }
    @Override
    @Transactional
    public void markAsRead(
            Long notificationId,
            String username
    ) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        if (!notification.getUser().getUsername().equals(username)) {
            throw new RuntimeException(
                    "You are not allowed to modify this notification"
            );
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }
    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(String username) {

        return notificationRepository
                .countByUserUsernameAndReadFalse(username);
    }


}
