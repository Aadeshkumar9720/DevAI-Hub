package com.devaihub.backend.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private String type;

    private boolean read;

    // Used when CREATING a notification
    public NotificationResponse(
            String title,
            String message,
            String type
    ) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = false;
    }

    // Used when RETURNING a notification from database
    public NotificationResponse(
            Long id,
            String title,
            String message,
            String type,
            boolean read
    ) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = read;
    }
}
