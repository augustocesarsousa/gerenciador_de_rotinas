package com.acsousa.gerenciador_de_rotinas.domain.person.specifications;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import static com.acsousa.gerenciador_de_rotinas.domain.person.specifications.PersonSpecification.*;

@Getter
@Setter
public class PersonQueryFilter {
    private Long id;
    private String name;
    private PersonType type;
    private EntityStatus status;
    private String cpf;
    private String cnpj;

    public Specification<PersonModel> toSpecification() {
        return Specification.where(idEquals(id))
                .and(nameLikeIgnoreCase(name))
                .and(typeEquals(type))
                .and(statusEquals(status))
                .and(cpfEquals(cpf))
                .and(cnpjEquals(cnpj));
    }
}
