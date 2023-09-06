package com.freesocial.users.dto;

import com.freesocial.users.common.validation.PasswordValidation;
import com.freesocial.users.common.validation.UsernameValidation;
import lombok.Data;

@Data
public class UserAuthenticationDTO {

    @UsernameValidation
    private String username;

    @PasswordValidation
    private String password;

    @PasswordValidation
    private String passwordConfirm;

}
