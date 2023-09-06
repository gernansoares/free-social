package com.freesocial.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UsernameValidator implements ConstraintValidator<UsernameValidation, String> {

    public boolean isValid(String username, ConstraintValidatorContext cxt) {
        return Objects.nonNull(username) && username.length() >= 5 && username.length() <= 20;
    }

}