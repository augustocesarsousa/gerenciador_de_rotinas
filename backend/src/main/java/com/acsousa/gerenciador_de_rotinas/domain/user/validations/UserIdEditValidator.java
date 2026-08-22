package com.acsousa.gerenciador_de_rotinas.domain.user.validations;

import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserIdEditValidator implements ConstraintValidator<UserIdEditValid, Long> {
    private final UserRepository userRepository;

    @Override
    public void initialize(UserIdEditValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null || userRepository.findById(id).isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Usuário de edição não encontrado")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
