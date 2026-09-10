package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FindBankByIdUseCase {
    private final BankRepository bankRepository;

    @Transactional(readOnly = true)
    public BankResponseRecord execute(Long id) {
        return bankRepository.findById(id)
                .map(BankResponseRecord::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição bancária não encontrada com id: " + id));
    }
}
