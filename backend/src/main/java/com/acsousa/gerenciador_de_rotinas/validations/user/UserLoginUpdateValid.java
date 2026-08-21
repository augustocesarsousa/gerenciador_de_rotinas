package com.acsousa.gerenciador_de_rotinas.validations.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserLoginUpdateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginUpdateValid {
    String message() default "Invalid user login";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
