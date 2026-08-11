package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.AddMemberRequest;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.entity.ProjectMember;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.mapper.ProjectMemberMapper;
import com.devaihub.backend.repository.ProjectMemberRepository;
import com.devaihub.backend.repository.ProjectRepository;
import com.devaihub.backend.repository.UserRepository;
import com.devaihub.backend.response.ProjectMemberResponse;
import com.devaihub.backend.service.interfaces.ProjectMemberService;
import org.springframework.stereotype.Service;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.service.interfaces.ActivityService;

import java.util.List;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberMapper memberMapper;
    private final ActivityService activityService;
    private final NotificationService notificationService;
    public ProjectMemberServiceImpl(
            ProjectMemberRepository memberRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectMemberMapper memberMapper,ActivityService activityService,
            NotificationService notificationService
    ) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.memberMapper = memberMapper;
        this.activityService = activityService;
        this.notificationService=notificationService;
    }

    @Override
    @Transactional
    public ProjectMemberResponse addMember(
            Long projectId,
            AddMemberRequest request,
            String username
    ) {
        System.out.println("====================================");
        System.out.println("ADD MEMBER DEBUG");
        System.out.println("PROJECT ID = " + projectId);
        System.out.println("PROJECT COUNT = " + projectRepository.count());
        System.out.println("PROJECT EXISTS = " + projectRepository.existsById(projectId));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Only the project owner can add members");
        }

        if (memberRepository.existsByProjectIdAndUserUsername(
                projectId,
                request.getUsername()
        )) {
            throw new RuntimeException("User is already a project member");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectMember member = new ProjectMember();

        member.setProject(project);
        member.setUser(user);
        member.setRole(request.getRole());

        ProjectMember saved = memberRepository.save(member);
        activityService.logActivity(
                project,
                project.getOwner(),
                ActivityType.PROJECT_MEMBER_ADDED,
                "Member '" + user.getUsername() + "' added."
        );
        notificationService.sendNotification(
                new NotificationResponse(
                        "Project Member Added",
                        user.getUsername() + " joined the project as "
                                + saved.getRole().name(),
                        "MEMBER_ADDED"
                ),
                user.getUsername()
        );
        return memberMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getMembers(Long projectId) {
        System.out.println("========== GET MEMBERS SERVICE ==========");
        System.out.println("PROJECT ID = " + projectId);
        return memberRepository.findByProjectId(projectId)
                .stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void removeMember(Long projectMemberId, String username) {

        ProjectMember member = memberRepository.findById(projectMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!member.getProject().getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Only the project owner can remove members");
        }
        activityService.logActivity(
                member.getProject(),
                member.getProject().getOwner(),
                ActivityType.PROJECT_MEMBER_REMOVED,
                "Member '" + member.getUser().getUsername() + "' removed from the project."
        );
        notificationService.sendNotification(
                new NotificationResponse(
                        "Project Member Removed",
                        member.getUser().getUsername()
                                + " was removed from the project.",
                        "MEMBER_REMOVED"
                ),
                member.getUser().getUsername()
        );
        memberRepository.delete(member);
    }
}
