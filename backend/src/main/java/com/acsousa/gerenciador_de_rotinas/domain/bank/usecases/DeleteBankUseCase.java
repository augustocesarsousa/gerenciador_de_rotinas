package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteBankUseCase {
    private final BankRepository bankRepository;

    @Transactional
    public void execute(Long id) {
        log.info("Iniciando a exclusão da instituição bancária com id: {}", id);

        BankModel bankModel = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição bancária não encontrada com id: " + id));

        bankRepository.delete(bankModel);
        log.info("Instituição bancária com id {} excluída com sucesso", id);
    }
}
