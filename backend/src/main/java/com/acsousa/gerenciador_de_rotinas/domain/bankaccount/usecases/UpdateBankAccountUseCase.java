package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateBankAccountUseCase {
    private final BankAccountRepository bankAccountRepository;
    private final BankRepository bankRepository;

    @Transactional
    public BankAccountResponseRecord execute(Long id, BankAccountUpdateRecord updateRecord) {
        log.info("Iniciando atualização da conta bancária ID: {}", id);

        BankAccountModel entity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária não encontrada com o ID: " + id));

        BankModel bank = null;
        if (updateRecord.accountType() == BankAccountType.CASH_DESK) {
            log.debug("Conta ID {} alterada para CASH_DESK: limpando dados bancários.", id);
        } else {
            if (updateRecord.bankId() == null) {
                throw new BusinessValidationException("A instituição bancária é obrigatória para este tipo de conta.");
            }
            bank = bankRepository.findById(updateRecord.bankId())
                    .orElseThrow(() -> new ResourceNotFoundException("Banco não encontrado com o ID: " + updateRecord.bankId()));

            if (bank.getStatus() == EntityStatus.INACTIVE) {
                throw new BusinessValidationException("Não é possível vincular uma conta a uma instituição bancária inativa.");
            }

            if (updateRecord.agencyNumber() == null || updateRecord.agencyNumber().isBlank()) {
                throw new BusinessValidationException("O número da agência é obrigatório.");
            }
            if (updateRecord.accountNumber() == null || updateRecord.accountNumber().isBlank()) {
                throw new BusinessValidationException("O número da conta é obrigatório.");
            }
            if (updateRecord.accountDigit() == null || updateRecord.accountDigit().isBlank()) {
                throw new BusinessValidationException("O dígito da conta é obrigatório.");
            }

            boolean exists = bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
                    bank.getId(),
                    updateRecord.agencyNumber().trim(),
                    updateRecord.accountNumber().trim(),
                    EntityStatus.ACTIVE,
                    id
            );
            if (exists) {
                throw new AttributeAlreadyExistsException("Já existe outra conta bancária ativa cadastrada para este banco, agência e conta.");
            }
        }

        updateRecord.updateEntity(entity, bank);
        if (updateRecord.accountType() == BankAccountType.CASH_DESK) {
            entity.setBank(null);
            entity.setAgencyNumber(null);
            entity.setAgencyDigit(null);
            entity.setAccountNumber(null);
            entity.setAccountDigit(null);
        }

        BankAccountModel updated = bankAccountRepository.save(entity);
        log.info("Conta bancária ID {} atualizada com sucesso.", updated.getId());
        return BankAccountResponseRecord.fromEntity(updated);
    }
}
