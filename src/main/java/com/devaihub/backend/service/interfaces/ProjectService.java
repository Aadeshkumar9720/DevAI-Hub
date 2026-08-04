package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request, String username);

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(Long id);
}
