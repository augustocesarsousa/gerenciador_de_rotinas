package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateBankUseCase {
    private final BankRepository bankRepository;

    @Transactional
    public BankResponseRecord execute(Long id, BankUpdateRecord updateRecord) {
        log.info("Iniciando a atualização da instituição bancária com id: {}", id);

        BankModel bankModel = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição bancária não encontrada com id: " + id));

        if (updateRecord.code() == null || !updateRecord.code().matches("^\\d{3}$")) {
            throw new BusinessValidationException("Código COMPE deve conter exatamente 3 dígitos numéricos");
        }

        String normalizedCode = updateRecord.code().trim();

        if (bankRepository.existsByCodeAndIdNot(normalizedCode, id)) {
            throw new AttributeAlreadyExistsException("Código COMPE " + normalizedCode + " já cadastrado para outra instituição bancária");
        }

        updateRecord.updateEntity(bankModel);
        bankModel.setCode(normalizedCode);

        BankModel updatedBank = bankRepository.save(bankModel);
        log.info("Instituição bancária com id: {} atualizada com sucesso", updatedBank.getId());

        return BankResponseRecord.fromEntity(updatedBank);
    }
}
