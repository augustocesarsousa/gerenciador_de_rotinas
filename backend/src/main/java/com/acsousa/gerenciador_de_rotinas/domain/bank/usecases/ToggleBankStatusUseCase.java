package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ToggleBankStatusUseCase {
    private final BankRepository bankRepository;

    @Transactional
    public BankResponseRecord execute(Long id, Long userIdEdit) {
        log.info("Alternando status da instituição bancária com id: {}", id);

        BankModel bankModel = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição bancária não encontrada com id: " + id));

        EntityStatus newStatus = bankModel.getStatus() == EntityStatus.ACTIVE 
                ? EntityStatus.INACTIVE 
                : EntityStatus.ACTIVE;

        bankModel.setStatus(newStatus);
        if (userIdEdit != null) {
            bankModel.setUserIdEdit(userIdEdit);
        }

        BankModel updated = bankRepository.save(bankModel);
        log.info("Status da instituição bancária {} alterado para {}", id, newStatus);

        return BankResponseRecord.fromEntity(updated);
    }
}
