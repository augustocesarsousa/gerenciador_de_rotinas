package com.acsousa.gerenciador_de_rotinas.domain.user.validations;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;

public class UserEmailUpdateValidator implements ConstraintValidator<UserEmailUpdateValid, String> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void initialize(UserEmailUpdateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Map<String, String> uriVars = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long userIdRequest = Long.parseLong(uriVars.get("id"));
        UserModel userModel = userRepository.findByEmail(email);

        if (Objects.nonNull(userModel) && !Objects.equals(userModel.getId(), userIdRequest)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("E-mail já cadastrado para outro usuário")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
