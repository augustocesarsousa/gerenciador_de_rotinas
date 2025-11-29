package com.acsousa.gerenciador_de_rotinas.validations.user;

import com.acsousa.gerenciador_de_rotinas.exceptions.handler.FieldMessage;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserIdEditValidator implements ConstraintValidator<UserIdEditValid, Long> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserIdEditValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        List<FieldMessage> fieldMessageList = new ArrayList<>();

        if(userRepository.findById(id).isEmpty()){
            fieldMessageList.add(new FieldMessage(null, "Usuário de edição não encontrado"));
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
