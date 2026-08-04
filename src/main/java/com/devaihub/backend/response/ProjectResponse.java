package com.devaihub.backend.response;

import com.devaihub.backend.entity.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {

    private Long id;

    private String name;

    private String description;

    private ProjectStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserSummaryResponse owner;
}
