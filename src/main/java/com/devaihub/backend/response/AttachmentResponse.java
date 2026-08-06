package com.devaihub.backend.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentResponse {

    private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String filePath;

    private UserSummaryResponse uploadedBy;
}