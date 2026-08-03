package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.service.interfaces.ProjectService;
import org.springframework.stereotype.Service;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.UserRepository;


@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Project createProject(CreateProjectRequest request, String username) {

        User owner = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setOwner(owner);

        return projectRepository.save(project);
    }
}
