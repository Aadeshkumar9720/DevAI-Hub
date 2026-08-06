package com.devaihub.backend.mapper;

import com.devaihub.backend.entity.Activity;
import com.devaihub.backend.response.ActivityResponse;
import com.devaihub.backend.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public ActivityResponse toResponse(Activity activity) {

        ActivityResponse response = new ActivityResponse();

        response.setId(activity.getId());
        response.setType(activity.getType().name());
        response.setDescription(activity.getDescription());
        response.setCreatedAt(activity.getCreatedAt());

        UserSummaryResponse user = new UserSummaryResponse();

        user.setId(activity.getPerformedBy().getId());
        user.setUsername(activity.getPerformedBy().getUsername());
        user.setFirstName(activity.getPerformedBy().getFirstName());
        user.setLastName(activity.getPerformedBy().getLastName());
        user.setRole(activity.getPerformedBy().getRole().name());

        response.setPerformedBy(user);

        return response;
    }
}
