package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - FindBankAccountByIdUseCase")
class FindBankAccountByIdUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private FindBankAccountByIdUseCase findBankAccountByIdUseCase;

    private BankAccountModel account;

    @BeforeEach
    void setUp() {
        BankModel bank = new BankModel();
        bank.setId(1L);
        bank.setShortName("Bradesco");
        bank.setCode("237");
        account = BankAccountFactory.createBankAccountModel(bank);
    }

    @Test
    @DisplayName("Deve retornar BankAccountResponseRecord quando o ID existir")
    void shouldReturnAccountWhenIdExists() {
        // given
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // when
        BankAccountResponseRecord response = findBankAccountByIdUseCase.execute(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.description()).isEqualTo("Conta Principal - Bradesco");
        assertThat(response.bankName()).isEqualTo("Bradesco");
        assertThat(response.bankCode()).isEqualTo("237");
        verify(bankAccountRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID não existir")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // given
        when(bankAccountRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> findBankAccountByIdUseCase.execute(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
        verify(bankAccountRepository, times(1)).findById(999L);
    }
}
