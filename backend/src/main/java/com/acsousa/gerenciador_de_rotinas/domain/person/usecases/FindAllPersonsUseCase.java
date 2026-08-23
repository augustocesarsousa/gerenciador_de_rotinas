package com.acsousa.gerenciador_de_rotinas.domain.person.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.repositories.PersonRepository;
import com.acsousa.gerenciador_de_rotinas.domain.person.specifications.PersonQueryFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllPersonsUseCase {
    private final PersonRepository personRepository;

    public Page<PersonResponseRecord> execute(PersonQueryFilter filter, Pageable pageable) {
        log.info("Listando pessoas de forma paginada aplicando filtros de consulta");
        Page<PersonModel> page = personRepository.findAll(filter.toSpecification(), pageable);
        return page.map(PersonResponseRecord::fromEntity);
    }
}
