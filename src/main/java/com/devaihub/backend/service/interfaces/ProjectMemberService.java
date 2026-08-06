package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.dto.AddMemberRequest;
import com.devaihub.backend.response.ProjectMemberResponse;

import java.util.List;

public interface ProjectMemberService {

    ProjectMemberResponse addMember(
            Long projectId,
            AddMemberRequest request,
            String username
    );

    List<ProjectMemberResponse> getMembers(Long projectId);

    void removeMember(
            Long projectMemberId,
            String username
    );
}
