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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

        existingAccount = new BankAccountModel(
                10L,
                activeBank,
                BankAccountType.CHECKING,
                "Conta Antiga",
                "1234",
                "0",
                "123456",
                "7",
                null,
                new BigDecimal("100.00"),
                LocalDate.now(),
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );

        updateRecord = new BankAccountUpdateRecord(
                1L,
                BankAccountType.CHECKING,
                "Conta Atualizada",
                "1234",
                "0",
                "123456",
                "7",
                "Novo Convênio",
                new BigDecimal("200.00"),
                LocalDate.now(),
                EntityStatus.ACTIVE,
                1L
        );
    }

    @Test
    void shouldUpdateBankAccountWhenDataIsValid() {
        when(bankAccountRepository.findById(10L)).thenReturn(Optional.of(existingAccount));
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
                1L, "1234", "123456", EntityStatus.ACTIVE, 10L
        )).thenReturn(false);
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenAnswer(i -> i.getArgument(0));

        BankAccountResponseRecord response = updateBankAccountUseCase.execute(10L, updateRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(10L, response.id());
        Assertions.assertEquals("Conta Atualizada", response.description());
        Assertions.assertEquals("Novo Convênio", response.projectOrAgreement());

        verify(bankAccountRepository, times(1)).save(any(BankAccountModel.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAccountNotFound() {
        when(bankAccountRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                updateBankAccountUseCase.execute(999L, updateRecord)
        );

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void shouldThrowAttributeAlreadyExistsExceptionWhenActiveAccountExistsForOtherId() {
        when(bankAccountRepository.findById(10L)).thenReturn(Optional.of(existingAccount));
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
                1L, "1234", "123456", EntityStatus.ACTIVE, 10L
        )).thenReturn(true);

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () ->
                updateBankAccountUseCase.execute(10L, updateRecord)
        );

        verify(bankAccountRepository, never()).save(any());
    }
}
