package com.freesocial.users.dto;

import com.freesocial.users.common.validation.BioValidation;
import com.freesocial.users.common.validation.NameValidation;
import com.freesocial.users.common.validation.PasswordValidation;
import com.freesocial.users.common.validation.UsernameValidation;
import lombok.Data;

@Data
public class UserSignUpDTO {

    @NameValidation
    private String name;

    @BioValidation
    private String bio;

    @UsernameValidation
    private String username;

    @PasswordValidation
    private String password;

    @PasswordValidation
    private String passwordConfirm;

}
