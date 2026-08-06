package com.devaihub.backend.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActivityResponse {

    private Long id;

    private String type;

    private String description;

    private UserSummaryResponse performedBy;

    private LocalDateTime createdAt;
}
