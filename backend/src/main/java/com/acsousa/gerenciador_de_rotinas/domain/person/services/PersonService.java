package com.acsousa.gerenciador_de_rotinas.domain.person.services;

import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.specifications.PersonQueryFilter;
import com.acsousa.gerenciador_de_rotinas.domain.person.usecases.CreatePersonUseCase;
import com.acsousa.gerenciador_de_rotinas.domain.person.usecases.FindAllPersonsUseCase;
import com.acsousa.gerenciador_de_rotinas.domain.person.usecases.FindPersonByIdUseCase;
import com.acsousa.gerenciador_de_rotinas.domain.person.usecases.UpdatePersonUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final CreatePersonUseCase createPersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final FindAllPersonsUseCase findAllPersonsUseCase;

    public PersonResponseRecord create(PersonCreateRecord createRecord) {
        return createPersonUseCase.execute(createRecord);
    }

    public PersonResponseRecord update(Long id, PersonUpdateRecord updateRecord) {
        return updatePersonUseCase.execute(id, updateRecord);
    }

    public PersonResponseRecord findById(Long id) {
        return findPersonByIdUseCase.execute(id);
    }

    public Page<PersonResponseRecord> findAll(PersonQueryFilter filter, Pageable pageable) {
        return findAllPersonsUseCase.execute(filter, pageable);
    }
}
