package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.mapper.ProjectMapper;
import com.devaihub.backend.response.ProjectResponse;
import com.devaihub.backend.service.interfaces.ProjectService;
import org.springframework.stereotype.Service;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.UserRepository;
import java.util.List;
import com.devaihub.backend.dto.UpdateProjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.devaihub.backend.service.interfaces.ActivityService;
import com.devaihub.backend.enums.ActivityType;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ActivityService activityService;
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository,
                              ProjectMapper projectMapper, ActivityService activityService) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
        this.activityService = activityService;
    }
    @Transactional
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
        activityService.logActivity(
                savedProject,
                owner,
                ActivityType.PROJECT_CREATED,
                "Project '" + savedProject.getName() + "' was created."
        );
        return projectMapper.toResponse(savedProject);
    }
    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        return projectMapper.toResponse(project);
    }
    @Transactional
    @Override
    public ProjectResponse updateProject(
            Long id,
            UpdateProjectRequest request,
            String username) {

        // Find project
        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // Check owner
        if (!project.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to update this project");
        }

        // Update fields
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        // Save and return DTO
        Project updatedProject = projectRepository.save(project);

        return projectMapper.toResponse(updatedProject);
    }
    @Transactional
    @Override
    public void deleteProject(
            Long projectId,
            String username) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found")
                );

        // Only project owner can delete the project
        if (!project.getOwner()
                .getUsername()
                .equals(username)) {

            throw new RuntimeException(
                    "You are not allowed to delete this project"
            );
        }

        projectRepository.delete(project);
    }
    @Transactional
    @Override
    public List<ProjectResponse> searchProjects(String keyword) {

        return projectRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }
    @Transactional
    @Override
    public Page<ProjectResponse> getProjects(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return projectRepository.findAll(pageable)
                .map(projectMapper::toResponse);
    }

}
