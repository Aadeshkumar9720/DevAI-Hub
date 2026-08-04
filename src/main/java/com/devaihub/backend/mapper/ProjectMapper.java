package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.Project;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.response.ProjectResponse;
import com.devaihub.backend.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project project) {

        ProjectResponse response = new ProjectResponse();

        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        UserSummaryResponse ownerResponse = new UserSummaryResponse();

        User owner = project.getOwner();

        ownerResponse.setId(owner.getId());
        ownerResponse.setUsername(owner.getUsername());
        ownerResponse.setFirstName(owner.getFirstName());
        ownerResponse.setLastName(owner.getLastName());
        ownerResponse.setRole(owner.getRole().name());

        response.setOwner(ownerResponse);

        return response;
    }
}
