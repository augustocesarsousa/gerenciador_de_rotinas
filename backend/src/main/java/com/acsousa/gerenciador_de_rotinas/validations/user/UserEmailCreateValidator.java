package com.acsousa.gerenciador_de_rotinas.validations.user;

import com.acsousa.gerenciador_de_rotinas.exceptions.handler.FieldMessage;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserEmailCreateValidator implements ConstraintValidator<UserEmailCreateValid, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserEmailCreateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        List<FieldMessage> fieldMessageList = new ArrayList<>();

        if (Objects.nonNull(userRepository.findByEmail(email))) {
            fieldMessageList.add(new FieldMessage(null, "E-mail já cadastrado para outro usuário"));
        }

        for (FieldMessage fieldMessage : fieldMessageList) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldMessage.getMessage())
                    .addPropertyNode(fieldMessage.getFieldName())
                    .addConstraintViolation();
        }

        return fieldMessageList.isEmpty();
    }
}
