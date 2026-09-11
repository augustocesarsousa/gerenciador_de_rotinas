package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
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
class CreateBankAccountUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private CreateBankAccountUseCase createBankAccountUseCase;

    private BankModel activeBank;
    private BankAccountCreateRecord validCheckingRecord;
    private BankAccountModel savedCheckingModel;

    @BeforeEach
    void setUp() {
        activeBank = new BankModel(
                1L, "001", "00000000", "Banco do Brasil S.A.", "Banco do Brasil",
                EntityStatus.ACTIVE, Instant.now(), Instant.now(), 1L
        );

        validCheckingRecord = new BankAccountCreateRecord(
                1L,
                BankAccountType.CHECKING,
                "Conta Corrente Principal",
                "1234",
                "0",
                "123456",
                "7",
                "Convênio 01/2026",
                new BigDecimal("100.00"),
                LocalDate.now(),
                EntityStatus.ACTIVE,
                1L
        );

        savedCheckingModel = new BankAccountModel(
                10L,
                activeBank,
                BankAccountType.CHECKING,
                "Conta Corrente Principal",
                "1234",
                "0",
                "123456",
                "7",
                "Convênio 01/2026",
                new BigDecimal("100.00"),
                LocalDate.now(),
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );
    }

    @Test
    void shouldCreateBankAccountWhenDataIsValid() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(1L, "1234", "123456", EntityStatus.ACTIVE))
                .thenReturn(false);
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenReturn(savedCheckingModel);

        BankAccountResponseRecord response = createBankAccountUseCase.execute(validCheckingRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(10L, response.id());
        Assertions.assertEquals(1L, response.bankId());
        Assertions.assertEquals("Conta Corrente Principal", response.description());
        Assertions.assertEquals(BankAccountType.CHECKING, response.accountType());

        verify(bankRepository, times(1)).findById(1L);
        verify(bankAccountRepository, times(1)).save(any(BankAccountModel.class));
    }

    @Test
    void shouldCreateCashDeskAccountWithoutBankDetails() {
        BankAccountCreateRecord cashDeskRecord = new BankAccountCreateRecord(
                null,
                BankAccountType.CASH_DESK,
                "Caixa Cantina",
                null,
                null,
                null,
                null,
                null,
                BigDecimal.ZERO,
                LocalDate.now(),
                EntityStatus.ACTIVE,
                1L
        );

        BankAccountModel savedCashDeskModel = new BankAccountModel(
                11L,
                null,
                BankAccountType.CASH_DESK,
                "Caixa Cantina",
                null,
                null,
                null,
                null,
                null,
                BigDecimal.ZERO,
                LocalDate.now(),
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );

        when(bankAccountRepository.save(any(BankAccountModel.class))).thenReturn(savedCashDeskModel);

        BankAccountResponseRecord response = createBankAccountUseCase.execute(cashDeskRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(11L, response.id());
        Assertions.assertNull(response.bankId());
        Assertions.assertEquals(BankAccountType.CASH_DESK, response.accountType());

        verify(bankRepository, never()).findById(any());
        verify(bankAccountRepository, times(1)).save(any(BankAccountModel.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenBankNotFound() {
        when(bankRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                createBankAccountUseCase.execute(validCheckingRecord)
        );

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessValidationExceptionWhenBankIsInactive() {
        BankModel inactiveBank = new BankModel(
                2L, "033", "00000000", "Santander", "Santander",
                EntityStatus.INACTIVE, Instant.now(), Instant.now(), 1L
        );
        when(bankRepository.findById(1L)).thenReturn(Optional.of(inactiveBank));

        Assertions.assertThrows(BusinessValidationException.class, () ->
                createBankAccountUseCase.execute(validCheckingRecord)
        );

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void shouldThrowAttributeAlreadyExistsExceptionWhenActiveAccountAlreadyExists() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(1L, "1234", "123456", EntityStatus.ACTIVE))
                .thenReturn(true);

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () ->
                createBankAccountUseCase.execute(validCheckingRecord)
        );

        verify(bankAccountRepository, never()).save(any());
    }
}
