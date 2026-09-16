package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - ToggleBankAccountStatusUseCase")
class ToggleBankAccountStatusUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private ToggleBankAccountStatusUseCase toggleBankAccountStatusUseCase;

    private BankAccountModel account;

    @BeforeEach
    void setUp() {
        BankModel bank = new BankModel();
        bank.setId(1L);
        account = BankAccountFactory.createBankAccountModel(bank);
    }

    @Test
    @DisplayName("Deve alternar status de ACTIVE para INACTIVE e nunca invocar delete físico")
    void shouldToggleStatusFromActiveToInactiveWithoutCallingDelete() {
        // given
        account.setStatus(EntityStatus.ACTIVE);
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenAnswer(i -> i.getArgument(0));

        // when
        BankAccountResponseRecord response = toggleBankAccountStatusUseCase.execute(1L, 2L);

        // then
        assertThat(response.status()).isEqualTo(EntityStatus.INACTIVE);
        assertThat(account.getUserIdEdit()).isEqualTo(2L);
        verify(bankAccountRepository, times(1)).save(account);
        verify(bankAccountRepository, never()).delete(any(BankAccountModel.class));
    }

    @Test
    @DisplayName("Deve alternar status de INACTIVE para ACTIVE com sucesso")
    void shouldToggleStatusFromInactiveToActive() {
        // given
        account.setStatus(EntityStatus.INACTIVE);
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenAnswer(i -> i.getArgument(0));

        // when
        BankAccountResponseRecord response = toggleBankAccountStatusUseCase.execute(1L, 1L);

        // then
        assertThat(response.status()).isEqualTo(EntityStatus.ACTIVE);
        verify(bankAccountRepository, times(1)).save(account);
        verify(bankAccountRepository, never()).delete(any(BankAccountModel.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID for inexistente")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // given
        when(bankAccountRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> toggleBankAccountStatusUseCase.execute(999L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");

        verify(bankAccountRepository, never()).save(any());
    }
}
