package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateBankAccountUseCase {
    private final BankAccountRepository bankAccountRepository;
    private final BankRepository bankRepository;

    @Transactional
    public BankAccountResponseRecord execute(BankAccountCreateRecord createRecord) {
        log.info("Iniciando criação de conta bancária: {}", createRecord.description());

        BankModel bank = null;
        if (createRecord.accountType() == BankAccountType.CASH_DESK) {
            log.debug("Conta do tipo CASH_DESK: campos bancários serão desconsiderados.");
        } else {
            if (createRecord.bankId() == null) {
                throw new BusinessValidationException("A instituição bancária é obrigatória para este tipo de conta.");
            }
            bank = bankRepository.findById(createRecord.bankId())
                    .orElseThrow(() -> new ResourceNotFoundException("Banco não encontrado com o ID: " + createRecord.bankId()));

            if (bank.getStatus() == EntityStatus.INACTIVE) {
                throw new BusinessValidationException("Não é possível vincular uma conta a uma instituição bancária inativa.");
            }

            if (createRecord.agencyNumber() == null || createRecord.agencyNumber().isBlank()) {
                throw new BusinessValidationException("O número da agência é obrigatório.");
            }
            if (createRecord.accountNumber() == null || createRecord.accountNumber().isBlank()) {
                throw new BusinessValidationException("O número da conta é obrigatório.");
            }
            if (createRecord.accountDigit() == null || createRecord.accountDigit().isBlank()) {
                throw new BusinessValidationException("O dígito da conta é obrigatório.");
            }

            boolean exists = bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(
                    bank.getId(),
                    createRecord.agencyNumber().trim(),
                    createRecord.accountNumber().trim(),
                    EntityStatus.ACTIVE
            );
            if (exists) {
                throw new AttributeAlreadyExistsException("Já existe uma conta bancária ativa cadastrada para este banco, agência e conta.");
            }
        }

        BankAccountModel entity = createRecord.toEntity(bank);
        if (createRecord.accountType() == BankAccountType.CASH_DESK) {
            entity.setBank(null);
            entity.setAgencyNumber(null);
            entity.setAgencyDigit(null);
            entity.setAccountNumber(null);
            entity.setAccountDigit(null);
        }

        BankAccountModel saved = bankAccountRepository.save(entity);
        log.info("Conta bancária criada com sucesso. ID: {}", saved.getId());
        return BankAccountResponseRecord.fromEntity(saved);
    }
}
