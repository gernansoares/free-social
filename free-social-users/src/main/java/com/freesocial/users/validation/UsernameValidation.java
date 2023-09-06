package com.freesocial.users.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameValidation {

    public String message() default "Invalid username, must have between ";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}