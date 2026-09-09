package com.acsousa.gerenciador_de_rotinas.domain.person.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindPersonByIdUseCase {
    private final PersonRepository personRepository;

    public PersonResponseRecord execute(Long id) {
        log.info("Buscando pessoa id: {}", id);
        PersonModel person = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pessoa não encontrada com id: " + id));
        return PersonResponseRecord.fromEntity(person);
    }
}
