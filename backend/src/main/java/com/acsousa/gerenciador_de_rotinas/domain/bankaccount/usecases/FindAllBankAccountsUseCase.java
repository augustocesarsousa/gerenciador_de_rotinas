package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications.BankAccountQueryFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllBankAccountsUseCase {
    private final BankAccountRepository bankAccountRepository;

    @Transactional(readOnly = true)
    public Page<BankAccountResponseRecord> execute(BankAccountQueryFilter filter, Pageable pageable) {
        log.debug("Listando contas bancárias paginadas");
        Page<BankAccountModel> page = bankAccountRepository.findAll(
                filter != null ? filter.toSpecification() : null,
                pageable
        );
        return page.map(BankAccountResponseRecord::fromEntity);
    }
}
