package com.acsousa.gerenciador_de_rotinas.domain.bank.services;

import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.specifications.BankQueryFilter;
import com.acsousa.gerenciador_de_rotinas.domain.bank.usecases.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankService {
    private final CreateBankUseCase createBankUseCase;
    private final UpdateBankUseCase updateBankUseCase;
    private final FindBankByIdUseCase findBankByIdUseCase;
    private final FindAllBanksUseCase findAllBanksUseCase;
    private final ToggleBankStatusUseCase toggleBankStatusUseCase;
    private final DeleteBankUseCase deleteBankUseCase;

    public BankResponseRecord create(BankCreateRecord createRecord) {
        return createBankUseCase.execute(createRecord);
    }

    public BankResponseRecord update(Long id, BankUpdateRecord updateRecord) {
        return updateBankUseCase.execute(id, updateRecord);
    }

    public BankResponseRecord findById(Long id) {
        return findBankByIdUseCase.execute(id);
    }

    public Page<BankResponseRecord> findAll(BankQueryFilter filter, Pageable pageable) {
        return findAllBanksUseCase.execute(filter, pageable);
    }

    public BankResponseRecord toggleStatus(Long id, Long userIdEdit) {
        return toggleBankStatusUseCase.execute(id, userIdEdit);
    }

    public void delete(Long id) {
        deleteBankUseCase.execute(id);
    }
}
