package com.acsousa.gerenciador_de_rotinas.domain.person.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.common.utils.CpfCnpjValidator;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePersonUseCase {
    private final PersonRepository personRepository;

    @Transactional
    public PersonResponseRecord execute(Long id, PersonUpdateRecord updateRecord) {
        log.info("Iniciando a atualização da pessoa id: {}", id);

        PersonModel person = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pessoa não encontrada com id: " + id));

        if (updateRecord.type() == null) {
            throw new BusinessValidationException("Tipo de pessoa é obrigatório");
        }

        person.setName(updateRecord.name());
        person.setType(updateRecord.type());
        person.setStatus(updateRecord.status());
        person.setAddress(updateRecord.address());
        person.setNumber(updateRecord.number());
        person.setNeighborhood(updateRecord.neighborhood());
        person.setCity(updateRecord.city());
        person.setState(updateRecord.state());
        person.setZipcode(updateRecord.zipcode());
        person.setPhone(updateRecord.phone());
        person.setEmail(updateRecord.email());
        person.setUserIdEdit(updateRecord.userIdEdit());

        if (updateRecord.type() == PersonType.PHYSICAL) {
            String cpf = updateRecord.cpf();
            if (cpf == null || cpf.isBlank()) {
                throw new BusinessValidationException("CPF é obrigatório para pessoa física");
            }
            if (!CpfCnpjValidator.isValidCpf(cpf)) {
                throw new BusinessValidationException("CPF inválido");
            }
            String cleanCpf = cpf.replaceAll("\\D", "");
            person.setCpf(cleanCpf);
            person.setCnpj(null);

            personRepository.findByCpf(cleanCpf).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new AttributeAlreadyExistsException("CPF já cadastrado para outra pessoa");
                }
            });
        } else if (updateRecord.type() == PersonType.LEGAL) {
            String cnpj = updateRecord.cnpj();
            if (cnpj == null || cnpj.isBlank()) {
                throw new BusinessValidationException("CNPJ é obrigatório para pessoa jurídica");
            }
            if (!CpfCnpjValidator.isValidCnpj(cnpj)) {
                throw new BusinessValidationException("CNPJ inválido");
            }
            String cleanCnpj = cnpj.replaceAll("\\D", "");
            person.setCnpj(cleanCnpj);
            person.setCpf(null);

            personRepository.findByCnpj(cleanCnpj).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new AttributeAlreadyExistsException("CNPJ já cadastrado para outra pessoa");
                }
            });
        }

        PersonModel savedPerson = personRepository.save(person);
        log.info("Pessoa atualizada com sucesso para id: {}", savedPerson.getId());
        return PersonResponseRecord.fromEntity(savedPerson);
    }
}
