package com.freesocial.users.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {

    @NotNull
    @Size(min = 1, max = 255, message = "{invalid.name}")
    private String name;

    @Size(min = 0, max = 500, message = "{invalid.bio}")
    private String bio;

}
