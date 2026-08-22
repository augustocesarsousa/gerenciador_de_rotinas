package com.acsousa.gerenciador_de_rotinas.domain.user.validations;

import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class UserEmailCreateValidator implements ConstraintValidator<UserEmailCreateValid, String> {
    private final UserRepository userRepository;

    @Override
    public void initialize(UserEmailCreateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (Objects.nonNull(userRepository.findByEmail(email))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("E-mail já cadastrado para outro usuário")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
