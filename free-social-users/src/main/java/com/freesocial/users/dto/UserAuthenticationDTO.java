package com.freesocial.users.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticationDTO {

    @NotNull
    @Size(min = 5, max = 20, message = "{invalid.username}")
    private String username;

    @NotNull
    @Size(min = 6, max = 12, message = "{invalid.password}")
    private String password;

}
