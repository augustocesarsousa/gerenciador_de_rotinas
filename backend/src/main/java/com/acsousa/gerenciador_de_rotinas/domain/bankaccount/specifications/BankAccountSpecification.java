package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import org.springframework.data.jpa.domain.Specification;

public class BankAccountSpecification {
    public static Specification<BankAccountModel> idEquals(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<BankAccountModel> bankIdEquals(Long bankId) {
        return (root, query, criteriaBuilder) ->
                bankId == null ? null : criteriaBuilder.equal(root.get("bank").get("id"), bankId);
    }

    public static Specification<BankAccountModel> accountTypeEquals(BankAccountType accountType) {
        return (root, query, criteriaBuilder) ->
                accountType == null ? null : criteriaBuilder.equal(root.get("accountType"), accountType);
    }

    public static Specification<BankAccountModel> descriptionLikeIgnoreCase(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase().trim() + "%");
        };
    }

    public static Specification<BankAccountModel> statusEquals(EntityStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<BankAccountModel> searchLikeIgnoreCase(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) {
                return null;
            }
            String pattern = "%" + search.toLowerCase().trim() + "%";
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("agencyNumber")), pattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("accountNumber")), pattern)
            );
        };
    }
}
