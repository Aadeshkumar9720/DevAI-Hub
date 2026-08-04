package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.entity.Project;
import java.util.List;
public interface ProjectService {

    Project createProject(CreateProjectRequest request, String username);

    List<Project> getAllProjects();

    Project getProjectById(Long id);
}
