package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - UpdateBankAccountUseCase")
class UpdateBankAccountUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private UpdateBankAccountUseCase updateBankAccountUseCase;

    private BankModel activeBank;
    private BankAccountModel existingAccount;
    private BankAccountUpdateRecord updateRecord;

    @BeforeEach
    void setUp() {
        activeBank = new BankModel(
                1L, "001", "00000000", "Banco do Brasil S.A.", "Banco do Brasil",
                EntityStatus.ACTIVE, Instant.now(), Instant.now(), 1L
        );
        existingAccount = BankAccountFactory.createBankAccountModel(activeBank);
        updateRecord = BankAccountFactory.createBankAccountUpdateRecord(1L);
    }

    @Test
    @DisplayName("Deve atualizar dados cadastrais com sucesso quando os dados forem válidos")
    void shouldUpdateBankAccountWhenDataIsValid() {
        // given
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
                1L, "1234", "123456", EntityStatus.ACTIVE, 1L
        )).thenReturn(false);
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenAnswer(i -> i.getArgument(0));

        // when
        BankAccountResponseRecord response = updateBankAccountUseCase.execute(1L, updateRecord);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.description()).isEqualTo("Conta Principal Atualizada");
        assertThat(response.projectOrAgreement()).isEqualTo("Novo Convênio 2026");

        verify(bankAccountRepository, times(1)).save(any(BankAccountModel.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID da conta não for encontrado")
    void shouldThrowResourceNotFoundExceptionWhenAccountNotFound() {
        // given
        when(bankAccountRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> updateBankAccountUseCase.execute(999L, updateRecord))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar AttributeAlreadyExistsException quando novos dados conflitarem com outra conta ativa")
    void shouldThrowAttributeAlreadyExistsExceptionWhenActiveAccountExistsForOtherId() {
        // given
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
                1L, "1234", "123456", EntityStatus.ACTIVE, 1L
        )).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> updateBankAccountUseCase.execute(1L, updateRecord))
                .isInstanceOf(AttributeAlreadyExistsException.class)
                .hasMessageContaining("Já existe outra conta bancária ativa");

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve converter conta convencional para CASH_DESK limpando dados bancários")
    void shouldConvertAccountToCashDeskClearingBankData() {
        // given
        BankAccountUpdateRecord cashDeskUpdate = new BankAccountUpdateRecord(
                null, BankAccountType.CASH_DESK, "Caixa Convertido", null, null, null, null, null,
                new BigDecimal("1500.00"), LocalDate.of(2026, 1, 1), EntityStatus.ACTIVE, 1L
        );
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenAnswer(i -> i.getArgument(0));

        // when
        BankAccountResponseRecord response = updateBankAccountUseCase.execute(1L, cashDeskUpdate);

        // then
        assertThat(response.accountType()).isEqualTo(BankAccountType.CASH_DESK);
        assertThat(response.bankId()).isNull();
        assertThat(response.agencyNumber()).isNull();
        assertThat(response.accountNumber()).isNull();
        verify(bankRepository, never()).findById(any());
        verify(bankAccountRepository).save(any(BankAccountModel.class));
    }
}
