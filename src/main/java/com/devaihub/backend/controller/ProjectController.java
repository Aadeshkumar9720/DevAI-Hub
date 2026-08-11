package com.devaihub.backend.controller;

import com.devaihub.backend.dto.CreateProjectRequest;
import com.devaihub.backend.response.ProjectResponse;
import com.devaihub.backend.service.interfaces.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(
        name = "Project Management",
        description = "APIs for managing projects"
)
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @Operation(
            summary = "Create Project",
            description = "Creates a new project for the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid project details"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ProjectResponse createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {

        return projectService.createProject(
                request,
                authentication.getName()
        );
    }
    @Operation(
            summary = "Get All Projects",
            description = "Returns all projects accessible to the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projects fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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

    @Operation(
            summary = "Get Project",
            description = "Returns project details by project ID."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable("projectId") Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project fetched successfully",
                        projectService.getProjectById(id)
                )
        );
    }
    @Operation(
            summary = "Update Project",
            description = "Updates an existing project."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable("projectId") Long id,
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
    @Operation(
            summary = "Delete Project",
            description = "Deletes a project."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Project deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable("projectId") Long projectId,
            Authentication authentication) {

        projectService.deleteProject(
                projectId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project deleted successfully",
                        null
                )
        );
    }
    @Operation(
            summary = "Search Projects",
            description = "Search projects by keyword."
    )
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
    @Operation(
            summary = "Get Projects with Pagination",
            description = "Returns paginated and sorted projects."
    )
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