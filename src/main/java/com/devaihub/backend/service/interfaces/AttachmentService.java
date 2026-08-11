package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.response.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
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

    Resource getAttachmentFile(Long attachmentId);

    AttachmentResponse getAttachment(Long attachmentId);
}