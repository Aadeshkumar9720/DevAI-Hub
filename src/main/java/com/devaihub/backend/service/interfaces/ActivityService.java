package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.entity.Project;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.response.ActivityResponse;

import java.util.List;

public interface ActivityService {

    void logActivity(
            Project project,
            User user,
            ActivityType type,
            String description
    );

    List<ActivityResponse> getProjectActivities(Long projectId);
}
