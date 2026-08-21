package com.acsousa.gerenciador_de_rotinas.specifications.queryFilter;

import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import static com.acsousa.gerenciador_de_rotinas.specifications.UserSpecification.*;

@Data
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
