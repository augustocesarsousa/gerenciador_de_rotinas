package com.acsousa.gerenciador_de_rotinas.domain.person.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.utils.CpfCnpjValidator;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePersonUseCase {
    private final PersonRepository personRepository;

    @Transactional
    public PersonResponseRecord execute(PersonCreateRecord createRecord) {
        log.info("Iniciando a criação de uma nova pessoa: {}", createRecord.name());

        if (createRecord.type() == null) {
            throw new BusinessValidationException("Tipo de pessoa é obrigatório");
        }

        PersonModel personModel = createRecord.toEntity();

        if (createRecord.type() == PersonType.PHYSICAL) {
            String cpf = createRecord.cpf();
            if (cpf == null || cpf.isBlank()) {
                throw new BusinessValidationException("CPF é obrigatório para pessoa física");
            }
            if (!CpfCnpjValidator.isValidCpf(cpf)) {
                throw new BusinessValidationException("CPF inválido");
            }
            personModel.setCpf(cpf.replaceAll("\\D", ""));
            personModel.setCnpj(null);

            if (personRepository.findByCpf(personModel.getCpf()).isPresent()) {
                throw new AttributeAlreadyExistsException("CPF já cadastrado para outra pessoa");
            }
        } else if (createRecord.type() == PersonType.LEGAL) {
            String cnpj = createRecord.cnpj();
            if (cnpj == null || cnpj.isBlank()) {
                throw new BusinessValidationException("CNPJ é obrigatório para pessoa jurídica");
            }
            if (!CpfCnpjValidator.isValidCnpj(cnpj)) {
                throw new BusinessValidationException("CNPJ inválido");
            }
            personModel.setCnpj(cnpj.replaceAll("\\D", ""));
            personModel.setCpf(null);

            if (personRepository.findByCnpj(personModel.getCnpj()).isPresent()) {
                throw new AttributeAlreadyExistsException("CNPJ já cadastrado para outra pessoa");
            }
        }

        PersonModel savedPerson = personRepository.save(personModel);
        log.info("Pessoa criada com sucesso com id: {}", savedPerson.getId());
        return PersonResponseRecord.fromEntity(savedPerson);
    }
}
