package com.acsousa.gerenciador_de_rotinas.domain.user.specifications;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import static com.acsousa.gerenciador_de_rotinas.domain.user.specifications.UserSpecification.*;

@Getter
@Setter
public class UserQueryFilter {
    private Long id;
    private String name;
    private String login;
    private String email;
    private UserStatus status;

    public Specification<UserModel> toSpecification() {
        return idEquals(id)
                .and(nameLikeIgnoreCase(name))
                .and(loginLikeIgnoreCase(login))
                .and(emailLikeIgnoreCase(email))
                .and(statusEquals(status));
    }
}
