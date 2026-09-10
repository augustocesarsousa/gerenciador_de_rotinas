package com.acsousa.gerenciador_de_rotinas.domain.bank.specifications;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import static com.acsousa.gerenciador_de_rotinas.domain.bank.specifications.BankSpecification.*;

@Getter
@Setter
public class BankQueryFilter {
    private Long id;
    private String code;
    private String name;
    private String shortName;
    private String search;
    private EntityStatus status;

    public Specification<BankModel> toSpecification() {
        return Specification.where(idEquals(id))
                .and(codeEquals(code))
                .and(nameLikeIgnoreCase(name))
                .and(shortNameLikeIgnoreCase(shortName))
                .and(searchLikeIgnoreCase(search))
                .and(statusEquals(status));
    }
}
