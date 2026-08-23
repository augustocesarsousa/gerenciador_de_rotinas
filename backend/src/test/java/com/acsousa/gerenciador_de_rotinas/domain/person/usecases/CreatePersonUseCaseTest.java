package com.acsousa.gerenciador_de_rotinas.domain.person.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.repositories.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreatePersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CreatePersonUseCase createPersonUseCase;

    private PersonCreateRecord validPhysicalRecord;
    private PersonCreateRecord validLegalRecord;
    private PersonModel physicalModel;
    private PersonModel legalModel;

    // CPFs e CNPJs matematicamente válidos para testes
    private final String validCpf = "19100000000"; 
    private final String validCnpj = "12345678000195"; 

    @BeforeEach
    void setUp() {
        validPhysicalRecord = new PersonCreateRecord(
                "Clark Kent",
                PersonType.PHYSICAL,
                EntityStatus.ACTIVE,
                validCpf,
                null,
                "Rua de Smallville",
                10L,
                "Rural",
                "Metropolis",
                "KS",
                "66001-000",
                "11999998888",
                "clark.kent@email.com",
                1L
        );

        validLegalRecord = new PersonCreateRecord(
                "Daily Planet Corp",
                PersonType.LEGAL,
                EntityStatus.ACTIVE,
                null,
                validCnpj,
                "Av Metropolis",
                100L,
                "Downtown",
                "Metropolis",
                "KS",
                "66001-000",
                "1133334444",
                "daily.planet@email.com",
                1L
        );

        physicalModel = validPhysicalRecord.toEntity();
        physicalModel.setId(1L);

        legalModel = validLegalRecord.toEntity();
        legalModel.setId(2L);
    }

    @Test
    public void shouldCreatePhysicalPersonWhenValidCpf() {
        when(personRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(personRepository.save(any(PersonModel.class))).thenReturn(physicalModel);

        PersonResponseRecord response = createPersonUseCase.execute(validPhysicalRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.id());
        Assertions.assertEquals(PersonType.PHYSICAL, response.type());
        Assertions.assertEquals(validCpf, response.cpf());
        Assertions.assertNull(response.cnpj());
        verify(personRepository, times(1)).save(any(PersonModel.class));
    }

    @Test
    public void shouldCreateLegalPersonWhenValidCnpj() {
        when(personRepository.findByCnpj(any())).thenReturn(Optional.empty());
        when(personRepository.save(any(PersonModel.class))).thenReturn(legalModel);

        PersonResponseRecord response = createPersonUseCase.execute(validLegalRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2L, response.id());
        Assertions.assertEquals(PersonType.LEGAL, response.type());
        Assertions.assertEquals(validCnpj, response.cnpj());
        Assertions.assertNull(response.cpf());
        verify(personRepository, times(1)).save(any(PersonModel.class));
    }

    @Test
    public void shouldThrowBusinessValidationExceptionWhenPhysicalPersonCpfIsNull() {
        PersonCreateRecord invalidRecord = new PersonCreateRecord(
                "Clark", PersonType.PHYSICAL, EntityStatus.ACTIVE, null, null, null, null, null, null, null, null, null, null, 1L
        );

        Assertions.assertThrows(BusinessValidationException.class, () -> createPersonUseCase.execute(invalidRecord));
        verify(personRepository, never()).save(any());
    }

    @Test
    public void shouldThrowBusinessValidationExceptionWhenPhysicalPersonCpfIsInvalid() {
        PersonCreateRecord invalidRecord = new PersonCreateRecord(
                "Clark", PersonType.PHYSICAL, EntityStatus.ACTIVE, "12345678900", null, null, null, null, null, null, null, null, null, 1L
        );

        Assertions.assertThrows(BusinessValidationException.class, () -> createPersonUseCase.execute(invalidRecord));
        verify(personRepository, never()).save(any());
    }

    @Test
    public void shouldThrowBusinessValidationExceptionWhenLegalPersonCnpjIsNull() {
        PersonCreateRecord invalidRecord = new PersonCreateRecord(
                "Planet", PersonType.LEGAL, EntityStatus.ACTIVE, null, "", null, null, null, null, null, null, null, null, 1L
        );

        Assertions.assertThrows(BusinessValidationException.class, () -> createPersonUseCase.execute(invalidRecord));
        verify(personRepository, never()).save(any());
    }

    @Test
    public void shouldThrowBusinessValidationExceptionWhenLegalPersonCnpjIsInvalid() {
        PersonCreateRecord invalidRecord = new PersonCreateRecord(
                "Planet", PersonType.LEGAL, EntityStatus.ACTIVE, null, "12345678000100", null, null, null, null, null, null, null, null, 1L
        );

        Assertions.assertThrows(BusinessValidationException.class, () -> createPersonUseCase.execute(invalidRecord));
        verify(personRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAttributeAlreadyExistsExceptionWhenCpfAlreadyExists() {
        when(personRepository.findByCpf(any())).thenReturn(Optional.of(physicalModel));

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () -> createPersonUseCase.execute(validPhysicalRecord));
        verify(personRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAttributeAlreadyExistsExceptionWhenCnpjAlreadyExists() {
        when(personRepository.findByCnpj(any())).thenReturn(Optional.of(legalModel));

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () -> createPersonUseCase.execute(validLegalRecord));
        verify(personRepository, never()).save(any());
    }
}
