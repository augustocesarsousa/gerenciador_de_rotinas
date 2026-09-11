package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import static com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications.BankAccountSpecification.*;

@Getter
@Setter
public class BankAccountQueryFilter {
    private Long id;
    private Long bankId;
    private BankAccountType accountType;
    private String description;
    private EntityStatus status;
    private String search;

    public Specification<BankAccountModel> toSpecification() {
        return Specification.where(idEquals(id))
                .and(bankIdEquals(bankId))
                .and(accountTypeEquals(accountType))
                .and(descriptionLikeIgnoreCase(description))
                .and(statusEquals(status))
                .and(searchLikeIgnoreCase(search));
    }
}
