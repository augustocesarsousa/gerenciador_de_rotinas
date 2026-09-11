package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateBankUseCaseTest {

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private UpdateBankUseCase updateBankUseCase;

    private BankUpdateRecord updateRecord;
    private BankModel existingModel;

    @BeforeEach
    void setUp() {
        updateRecord = new BankUpdateRecord(
                "341",
                "60701190",
                "Itaú Unibanco S.A.",
                "Itaú",
                EntityStatus.ACTIVE,
                2L
        );

        existingModel = new BankModel(
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
    void shouldUpdateBankWhenDataIsValid() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(existingModel));
        when(bankRepository.existsByCodeAndIdNot("341", 1L)).thenReturn(false);
        when(bankRepository.save(any(BankModel.class))).thenReturn(existingModel);

        BankResponseRecord response = updateBankUseCase.execute(1L, updateRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("341", response.code());
        Assertions.assertEquals("Itaú Unibanco S.A.", response.name());
        Assertions.assertEquals("Itaú", response.shortName());

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, times(1)).existsByCodeAndIdNot("341", 1L);
        verify(bankRepository, times(1)).save(existingModel);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenBankDoesNotExist() {
        when(bankRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            updateBankUseCase.execute(99L, updateRecord);
        });

        verify(bankRepository, times(1)).findById(99L);
        verify(bankRepository, never()).save(any());
    }

    @Test
    void shouldThrowAttributeAlreadyExistsExceptionWhenCodeBelongsToAnotherBank() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(existingModel));
        when(bankRepository.existsByCodeAndIdNot("341", 1L)).thenReturn(true);

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () -> {
            updateBankUseCase.execute(1L, updateRecord);
        });

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, times(1)).existsByCodeAndIdNot("341", 1L);
        verify(bankRepository, never()).save(any());
    }
}
