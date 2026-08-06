package com.devaihub.backend.service.interfaces;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body
    );

    void sendHtmlEmail(
            String to,
            String subject,
            String html
    );
}
