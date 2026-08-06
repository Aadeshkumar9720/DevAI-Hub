package com.devaihub.backend.service.impl;

import com.devaihub.backend.entity.Activity;
import com.devaihub.backend.entity.Project;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.mapper.ActivityMapper;
import com.devaihub.backend.repository.ActivityRepository;
import com.devaihub.backend.response.ActivityResponse;
import com.devaihub.backend.service.interfaces.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(
            ActivityRepository activityRepository,
            ActivityMapper activityMapper
    ) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    @Override
    public void logActivity(
            Project project,
            User user,
            ActivityType type,
            String description
    ) {

        Activity activity = new Activity();

        activity.setProject(project);
        activity.setPerformedBy(user);
        activity.setType(type);
        activity.setDescription(description);

        activityRepository.save(activity);
    }

    @Override
    public List<ActivityResponse> getProjectActivities(Long projectId) {

        return activityRepository
                .findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(activityMapper::toResponse)
                .toList();
    }
}
