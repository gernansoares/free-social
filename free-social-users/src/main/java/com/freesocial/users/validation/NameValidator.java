package com.freesocial.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class NameValidator implements ConstraintValidator<NameValidation, String> {

    public boolean isValid(String name, ConstraintValidatorContext cxt) {
        return Objects.nonNull(name) && name.length() >= 1 && name.length() <= 255;
    }

}