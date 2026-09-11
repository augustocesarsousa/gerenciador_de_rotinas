package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateBankUseCaseTest {

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private CreateBankUseCase createBankUseCase;

    private BankCreateRecord validRecord;
    private BankModel savedModel;

    @BeforeEach
    void setUp() {
        validRecord = new BankCreateRecord(
                "001",
                "00000000",
                "Banco do Brasil S.A.",
                "Banco do Brasil",
                EntityStatus.ACTIVE,
                1L
        );

        savedModel = new BankModel(
                1L,
                "001",
                "00000000",
                "Banco do Brasil S.A.",
                "Banco do Brasil",
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );
    }

    @Test
    void shouldCreateBankWhenDataIsValid() {
        when(bankRepository.existsByCode("001")).thenReturn(false);
        when(bankRepository.save(any(BankModel.class))).thenReturn(savedModel);

        BankResponseRecord response = createBankUseCase.execute(validRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.id());
        Assertions.assertEquals("001", response.code());
        Assertions.assertEquals("Banco do Brasil S.A.", response.name());
        Assertions.assertEquals("Banco do Brasil", response.shortName());
        Assertions.assertEquals(EntityStatus.ACTIVE, response.status());

        verify(bankRepository, times(1)).existsByCode("001");
        verify(bankRepository, times(1)).save(any(BankModel.class));
    }

    @Test
    void shouldThrowAttributeAlreadyExistsExceptionWhenCodeAlreadyExists() {
        when(bankRepository.existsByCode("001")).thenReturn(true);

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () -> {
            createBankUseCase.execute(validRecord);
        });

        verify(bankRepository, times(1)).existsByCode("001");
        verify(bankRepository, never()).save(any(BankModel.class));
    }

    @Test
    void shouldThrowBusinessValidationExceptionWhenCodeIsInvalid() {
        BankCreateRecord invalidRecord = new BankCreateRecord(
                "12", // inválido, precisa de 3 dígitos
                null,
                "Banco Inválido",
                "Inválido",
                EntityStatus.ACTIVE,
                1L
        );

        Assertions.assertThrows(BusinessValidationException.class, () -> {
            createBankUseCase.execute(invalidRecord);
        });

        verify(bankRepository, never()).existsByCode(any());
        verify(bankRepository, never()).save(any(BankModel.class));
    }
}
