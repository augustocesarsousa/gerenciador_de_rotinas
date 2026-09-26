package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ToggleBankAccountStatusUseCase {
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public BankAccountResponseRecord execute(Long id, Long userIdEdit) {
        log.info("Alternando status da conta bancária ID: {}", id);

        BankAccountModel account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária não encontrada com o ID: " + id));

        EntityStatus newStatus = account.getStatus() == EntityStatus.ACTIVE
                ? EntityStatus.INACTIVE
                : EntityStatus.ACTIVE;

        account.setStatus(newStatus);
        if (userIdEdit != null) {
            account.setUserIdEdit(userIdEdit);
        }

        BankAccountModel updated = bankAccountRepository.save(account);
        log.info("Status da conta bancária ID {} alterado para {}", id, newStatus);
        return BankAccountResponseRecord.fromEntity(updated);
    }
}
