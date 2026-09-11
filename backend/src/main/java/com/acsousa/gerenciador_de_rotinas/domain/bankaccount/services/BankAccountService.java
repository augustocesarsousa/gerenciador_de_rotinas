package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.services;

import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications.BankAccountQueryFilter;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final CreateBankAccountUseCase createBankAccountUseCase;
    private final UpdateBankAccountUseCase updateBankAccountUseCase;
    private final FindBankAccountByIdUseCase findBankAccountByIdUseCase;
    private final FindAllBankAccountsUseCase findAllBankAccountsUseCase;
    private final ToggleBankAccountStatusUseCase toggleBankAccountStatusUseCase;
    private final DeleteBankAccountUseCase deleteBankAccountUseCase;

    public BankAccountResponseRecord create(BankAccountCreateRecord createRecord) {
        return createBankAccountUseCase.execute(createRecord);
    }

    public BankAccountResponseRecord update(Long id, BankAccountUpdateRecord updateRecord) {
        return updateBankAccountUseCase.execute(id, updateRecord);
    }

    public BankAccountResponseRecord findById(Long id) {
        return findBankAccountByIdUseCase.execute(id);
    }

    public Page<BankAccountResponseRecord> findAll(BankAccountQueryFilter filter, Pageable pageable) {
        return findAllBankAccountsUseCase.execute(filter, pageable);
    }

    public BankAccountResponseRecord toggleStatus(Long id, Long userIdEdit) {
        return toggleBankAccountStatusUseCase.execute(id, userIdEdit);
    }

    public void delete(Long id) {
        deleteBankAccountUseCase.execute(id);
    }
}
