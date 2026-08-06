package com.devaihub.backend.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMemberResponse {

    private Long id;

    private UserSummaryResponse user;

    private String role;
}
