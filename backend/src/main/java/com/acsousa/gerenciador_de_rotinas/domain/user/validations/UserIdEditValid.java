package com.acsousa.gerenciador_de_rotinas.domain.user.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserIdEditValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIdEditValid {
    String message() default "Invalid user id edit";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
