package com.devaihub.backend.controller;

import com.devaihub.backend.dto.AddMemberRequest;
import com.devaihub.backend.response.ApiResponse;
import com.devaihub.backend.response.ProjectMemberResponse;
import com.devaihub.backend.service.interfaces.ProjectMemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService memberService;

    public ProjectMemberController(ProjectMemberService memberService) {
        this.memberService = memberService;
    }

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
