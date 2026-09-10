package com.acsousa.gerenciador_de_rotinas.domain.bank.specifications;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class BankSpecification {

    public static Specification<BankModel> idEquals(Long id) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(id)) {
                return null;
            }
            return builder.equal(root.get("id"), id);
        };
    }

    public static Specification<BankModel> codeEquals(String code) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(code)) {
                return null;
            }
            return builder.equal(root.get("code"), code.trim());
        };
    }

    public static Specification<BankModel> nameLikeIgnoreCase(String name) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)) {
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    public static Specification<BankModel> shortNameLikeIgnoreCase(String shortName) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(shortName)) {
                return null;
            }
            return builder.like(builder.lower(root.get("shortName")), "%" + shortName.trim().toLowerCase() + "%");
        };
    }

    public static Specification<BankModel> statusEquals(EntityStatus status) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(status)) {
                return null;
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<BankModel> searchLikeIgnoreCase(String search) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(search)) {
                return null;
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return builder.or(
                builder.like(builder.lower(root.get("code")), pattern),
                builder.like(builder.lower(root.get("name")), pattern),
                builder.like(builder.lower(root.get("shortName")), pattern)
            );
        };
    }
}
