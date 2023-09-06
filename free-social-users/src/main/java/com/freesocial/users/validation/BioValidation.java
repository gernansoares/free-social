package com.freesocial.users.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BioValidator.class)
public @interface BioValidation {

    public String message() default "Invalid bio";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}