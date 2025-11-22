package com.acsousa.gerenciador_de_rotinas.specifications;

import com.acsousa.gerenciador_de_rotinas.enums.UserProfile;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class UserSpecification {
    public static Specification<UserModel> idEquals(Long id) {
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(id)) {
                return null;
            }
            return builder.equal(root.get("id"), id);
        };
    }

    public static Specification<UserModel> nameLikeIgnoreCase(String name) {
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(name)) {
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<UserModel> loginLikeIgnoreCase(String login) {
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(login)) {
                return null;
            }
            return builder.like(builder.lower(root.get("login")), "%" + login.toLowerCase() + "%");
        };
    }

    public static Specification<UserModel> emailLikeIgnoreCase(String email) {
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(email)) {
                return null;
            }
            return builder.like(builder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<UserModel> statusEquals(UserStatus status) {
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(status)) {
                return null;
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<UserModel> profileEquals(UserProfile profile) {
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(profile)) {
                return null;
            }
            return builder.equal(root.get("profile"), profile);
        };
    }
}
