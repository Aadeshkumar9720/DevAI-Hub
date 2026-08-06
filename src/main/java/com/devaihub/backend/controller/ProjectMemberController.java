package com.devaihub.backend.controller;

import com.devaihub.backend.dto.AddMemberRequest;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.ProjectMemberResponse;
import com.devaihub.backend.service.interfaces.ProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/members")
@Tag(
        name = "Project Members",
        description = "Manage project members"
)
public class ProjectMemberController {

    private final ProjectMemberService memberService;

    public ProjectMemberController(ProjectMemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(
            summary = "Add Member",
            description = "Adds a member to a project."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectMemberResponse>> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddMemberRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Member added successfully",
                        memberService.addMember(
                                projectId,
                                request,
                                authentication.getName()
                        )
                )
        );
    }

    @Operation(
            summary = "Get Members",
            description = "Returns all members of a project."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectMemberResponse>>> getMembers(
            @PathVariable Long projectId
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Project members fetched successfully",
                        memberService.getMembers(projectId)
                )
        );
    }

    @Operation(
            summary = "Remove Member",
            description = "Removes a member from a project."
    )
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<String>> removeMember(
            @PathVariable Long memberId,
            Authentication authentication
    ) {

        memberService.removeMember(
                memberId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Member removed successfully",
                        null
                )
        );
    }
}
