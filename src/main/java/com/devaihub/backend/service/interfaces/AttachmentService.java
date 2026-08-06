package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.response.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    AttachmentResponse uploadFile(
            Long taskId,
            MultipartFile file,
            String username
    );

    List<AttachmentResponse> getAttachments(Long taskId);

    void deleteAttachment(
            Long attachmentId,
            String username
    );
}