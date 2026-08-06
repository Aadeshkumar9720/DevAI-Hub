package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.ProjectMember;
import com.devaihub.backend.response.ProjectMemberResponse;
import com.devaihub.backend.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {

    public ProjectMemberResponse toResponse(ProjectMember member) {

        ProjectMemberResponse response = new ProjectMemberResponse();

        response.setId(member.getId());
        response.setRole(member.getRole().name());

        UserSummaryResponse user = new UserSummaryResponse();

        user.setId(member.getUser().getId());
        user.setUsername(member.getUser().getUsername());
        user.setFirstName(member.getUser().getFirstName());
        user.setLastName(member.getUser().getLastName());
        user.setRole(member.getUser().getRole().name());

        response.setUser(user);

        return response;
    }
}
