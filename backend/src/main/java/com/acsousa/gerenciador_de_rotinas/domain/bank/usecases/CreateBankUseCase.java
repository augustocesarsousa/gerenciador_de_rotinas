package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateBankUseCase {
    private final BankRepository bankRepository;

    @Transactional
    public BankResponseRecord execute(BankCreateRecord createRecord) {
        log.info("Iniciando a criação de uma nova instituição bancária: code={}, name={}", createRecord.code(), createRecord.name());

        if (createRecord.code() == null || !createRecord.code().matches("^\\d{3}$")) {
            throw new BusinessValidationException("Código COMPE deve conter exatamente 3 dígitos numéricos");
        }

        String normalizedCode = createRecord.code().trim();

        if (bankRepository.existsByCode(normalizedCode)) {
            throw new AttributeAlreadyExistsException("Código COMPE " + normalizedCode + " já cadastrado para outra instituição bancária");
        }

        BankModel bankModel = createRecord.toEntity();
        bankModel.setCode(normalizedCode);

        BankModel savedBank = bankRepository.save(bankModel);
        log.info("Instituição bancária criada com sucesso com id: {}", savedBank.getId());

        return BankResponseRecord.fromEntity(savedBank);
    }
}
