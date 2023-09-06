package com.freesocial.users.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class BioValidator implements ConstraintValidator<BioValidation, String> {

    public boolean isValid(String bio, ConstraintValidatorContext cxt) {
        return Objects.nonNull(bio) ? bio.length() <= 500 : true;
    }

}