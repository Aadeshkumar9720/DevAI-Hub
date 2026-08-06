package com.devaihub.backend.dto;

import com.devaihub.backend.enums.ProjectRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {

    @NotBlank
    private String username;

    @NotNull
    private ProjectRole role;
}
