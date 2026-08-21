package com.acsousa.gerenciador_de_rotinas.validations.user;

import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIdEditValidator implements ConstraintValidator<UserIdEditValid, Long> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserIdEditValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (userRepository.findById(id).isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Usuário de edição não encontrado")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
