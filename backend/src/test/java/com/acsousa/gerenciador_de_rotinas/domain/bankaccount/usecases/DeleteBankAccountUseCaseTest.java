package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - DeleteBankAccountUseCase")
class DeleteBankAccountUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private DeleteBankAccountUseCase deleteBankAccountUseCase;

    private BankAccountModel account;

    @BeforeEach
    void setUp() {
        BankModel bank = new BankModel();
        bank.setId(1L);
        account = BankAccountFactory.createBankAccountModel(bank);
    }

    @Test
    @DisplayName("Deve deletar conta bancária com sucesso quando ID existir")
    void shouldDeleteAccountWhenIdExists() {
        // given
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(bankAccountRepository).delete(account);

        // when
        deleteBankAccountUseCase.execute(1L);

        // then
        verify(bankAccountRepository, times(1)).findById(1L);
        verify(bankAccountRepository, times(1)).delete(account);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID não existir")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // given
        when(bankAccountRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> deleteBankAccountUseCase.execute(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");

        verify(bankAccountRepository, times(1)).findById(999L);
        verify(bankAccountRepository, never()).delete(any(BankAccountModel.class));
    }
}
