package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.Attachment;
import com.devaihub.backend.response.AttachmentResponse;
import com.devaihub.backend.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {

    public AttachmentResponse toResponse(Attachment attachment) {

        AttachmentResponse response = new AttachmentResponse();

        response.setId(attachment.getId());
        response.setFileName(attachment.getFileName());
        response.setFileType(attachment.getFileType());
        response.setFileSize(attachment.getFileSize());
        response.setFilePath(attachment.getFilePath());

        UserSummaryResponse user = new UserSummaryResponse();

        user.setId(attachment.getUploadedBy().getId());
        user.setUsername(attachment.getUploadedBy().getUsername());
        user.setFirstName(attachment.getUploadedBy().getFirstName());
        user.setLastName(attachment.getUploadedBy().getLastName());
        user.setRole(attachment.getUploadedBy().getRole().name());

        response.setUploadedBy(user);

        return response;
    }
}