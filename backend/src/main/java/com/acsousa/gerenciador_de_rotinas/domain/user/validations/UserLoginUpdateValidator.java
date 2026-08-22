package com.acsousa.gerenciador_de_rotinas.domain.user.validations;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class UserLoginUpdateValidator implements ConstraintValidator<UserLoginUpdateValid, String> {
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;

    @Override
    public void initialize(UserLoginUpdateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        @SuppressWarnings("unchecked")
        Map<String, String> uriVars = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long userIdRequest = Long.parseLong(uriVars.get("id"));
        UserModel userModel = userRepository.findByLogin(login);

        if (Objects.nonNull(userModel) && !Objects.equals(userModel.getId(), userIdRequest)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Login já cadastrado para outro usuário")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
