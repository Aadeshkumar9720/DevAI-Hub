package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.entity.Project;

public interface ProjectService {

    Project createProject(CreateProjectRequest request, String username);

}
