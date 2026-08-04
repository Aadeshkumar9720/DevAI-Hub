package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.mapper.ProjectMapper;
import com.devaihub.backend.response.ProjectResponse;
import com.devaihub.backend.service.interfaces.ProjectService;
import org.springframework.stereotype.Service;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.UserRepository;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository,
                              ProjectMapper projectMapper) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }
    @Override
    public ProjectResponse createProject(CreateProjectRequest request, String username) {

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

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponse(savedProject);
    }
    @Override
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        return projectMapper.toResponse(project);
    }
}
