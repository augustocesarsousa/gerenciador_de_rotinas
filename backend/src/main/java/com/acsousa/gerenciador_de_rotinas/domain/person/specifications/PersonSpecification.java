package com.acsousa.gerenciador_de_rotinas.domain.person.specifications;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class PersonSpecification {

    public static Specification<PersonModel> idEquals(Long id) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(id)) {
                return null;
            }
            return builder.equal(root.get("id"), id);
        };
    }

    public static Specification<PersonModel> nameLikeIgnoreCase(String name) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)) {
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<PersonModel> typeEquals(PersonType type) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(type)) {
                return null;
            }
            return builder.equal(root.get("type"), type);
        };
    }

    public static Specification<PersonModel> statusEquals(EntityStatus status) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(status)) {
                return null;
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<PersonModel> cpfEquals(String cpf) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(cpf)) {
                return null;
            }
            return builder.equal(root.get("cpf"), cpf.replaceAll("\\D", ""));
        };
    }

    public static Specification<PersonModel> cnpjEquals(String cnpj) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(cnpj)) {
                return null;
            }
            return builder.equal(root.get("cnpj"), cnpj.replaceAll("\\D", ""));
        };
    }
}
