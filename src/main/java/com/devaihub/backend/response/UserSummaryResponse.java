package com.devaihub.backend.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryResponse {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String role;
}
