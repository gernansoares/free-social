package com.freesocial.users.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NameValidator.class)
public @interface NameValidation {

    public String message() default "{invalid.name}";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}