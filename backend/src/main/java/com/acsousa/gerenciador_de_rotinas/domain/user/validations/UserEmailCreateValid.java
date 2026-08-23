package com.acsousa.gerenciador_de_rotinas.domain.user.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserEmailCreateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailCreateValid {
    String message() default "Invalid user email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
