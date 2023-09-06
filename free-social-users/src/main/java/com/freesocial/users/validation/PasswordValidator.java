package com.freesocial.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    public boolean isValid(String password, ConstraintValidatorContext cxt) {
        return Objects.nonNull(password) && password.length() >= 6 && password.length() <= 12;
    }

}