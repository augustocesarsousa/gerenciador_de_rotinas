package com.acsousa.gerenciador_de_rotinas.validations.user;

import com.acsousa.gerenciador_de_rotinas.exceptions.handler.FieldMessage;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
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
        List<FieldMessage> fieldMessageList = new ArrayList<>();

        if(Objects.nonNull(userRepository.findByLogin(login))){
            fieldMessageList.add(new FieldMessage(null, "Login já cadastrado para outro usuário"));
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
