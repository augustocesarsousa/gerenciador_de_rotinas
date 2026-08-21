package com.acsousa.gerenciador_de_rotinas.validations.user;

import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class UserLoginCreateValidator implements ConstraintValidator<UserLoginCreateValid, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserLoginCreateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (Objects.nonNull(userRepository.findByLogin(login))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Login já cadastrado para outro usuário")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
