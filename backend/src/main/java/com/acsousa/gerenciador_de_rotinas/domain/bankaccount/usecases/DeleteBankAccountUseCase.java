package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteBankAccountUseCase {
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public void execute(Long id) {
        log.info("Iniciando exclusão da conta bancária com ID: {}", id);

        BankAccountModel account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária não encontrada com o ID: " + id));

        bankAccountRepository.delete(account);
        log.info("Conta bancária com ID {} excluída com sucesso.", id);
    }
}
