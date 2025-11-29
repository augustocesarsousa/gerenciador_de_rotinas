package com.acsousa.gerenciador_de_rotinas.validations.user;

import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
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

        return Objects.nonNull(userModel) && Objects.equals(userModel.getId(), userIdRequest);
    }
}
