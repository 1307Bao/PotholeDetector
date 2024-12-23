package com.example.demo.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) // Where annotation can be defined
@Retention(RetentionPolicy.RUNTIME) // When annotation can be executed
@Constraint( // A class responsible for this annotation
    validatedBy = {EmailValidator.class}
)
public @interface EmailConstraint {
    String message() default "Invalid Email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
