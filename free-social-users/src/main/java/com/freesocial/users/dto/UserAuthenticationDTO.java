package com.freesocial.users.dto;

import com.freesocial.users.validation.PasswordValidation;
import com.freesocial.users.validation.UsernameValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserAuthenticationDTO {

    @UsernameValidation
    private String username;

    @PasswordValidation
    private String password;

    @PasswordValidation
    private String passwordConfirm;

}
