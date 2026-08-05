package com.devaihub.backend.controller;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.response.ProjectResponse;
import com.devaihub.backend.service.interfaces.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.devaihub.backend.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.devaihub.backend.dto.UpdateProjectRequest;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponse createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {

        return projectService.createProject(
                request,
                authentication.getName()
        );
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Projects fetched successfully",
                        projectService.getAllProjects()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project fetched successfully",
                        projectService.getProjectById(id)
                )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project updated successfully",
                        projectService.updateProject(
                                id,
                                request,
                                authentication.getName()
                        )
                )
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {

        projectService.deleteProject(id, authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project deleted successfully",
                        null
                )
        );
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> searchProjects(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Projects fetched successfully",
                        projectService.searchProjects(keyword)
                )
        );
    }
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<ProjectResponse>>> getProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Projects fetched successfully",
                        projectService.getProjects(
                                page,
                                size,
                                sortBy,
                                direction
                        )
                )
        );
    }
}