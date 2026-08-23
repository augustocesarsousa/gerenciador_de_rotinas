package com.acsousa.gerenciador_de_rotinas.domain.person.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonUpdateRecord;
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
public class UpdatePersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private UpdatePersonUseCase updatePersonUseCase;

    private PersonUpdateRecord validPhysicalRecord;
    private PersonModel physicalModel;
    private PersonModel otherPhysicalModel;
    private final String validCpf = "19100000000";
    private final String otherValidCpf = "28200000028";

    @BeforeEach
    void setUp() {
        validPhysicalRecord = new PersonUpdateRecord(
                "Clark Kent Update",
                PersonType.PHYSICAL,
                EntityStatus.ACTIVE,
                validCpf,
                null,
                "Rua de Smallville 2",
                20L,
                "Rural 2",
                "Metropolis 2",
                "KS",
                "66001-000",
                "11999998888",
                "clark.kent@email.com",
                1L
        );

        physicalModel = new PersonModel();
        physicalModel.setId(1L);
        physicalModel.setName("Clark Kent");
        physicalModel.setType(PersonType.PHYSICAL);
        physicalModel.setStatus(EntityStatus.ACTIVE);
        physicalModel.setCpf(validCpf);
        physicalModel.setCnpj(null);
        physicalModel.setUserIdEdit(1L);

        otherPhysicalModel = new PersonModel();
        otherPhysicalModel.setId(2L);
        otherPhysicalModel.setName("Other Person");
        otherPhysicalModel.setType(PersonType.PHYSICAL);
        otherPhysicalModel.setStatus(EntityStatus.ACTIVE);
        otherPhysicalModel.setCpf(otherValidCpf);
        otherPhysicalModel.setUserIdEdit(1L);
    }

    @Test
    public void shouldUpdatePersonWhenValidData() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(physicalModel));
        when(personRepository.findByCpf(any())).thenReturn(Optional.of(physicalModel)); // encontra a si mesmo
        when(personRepository.save(any(PersonModel.class))).thenReturn(physicalModel);

        PersonResponseRecord response = updatePersonUseCase.execute(1L, validPhysicalRecord);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.id());
        Assertions.assertEquals("Clark Kent Update", response.name());
        verify(personRepository, times(1)).save(any(PersonModel.class));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenNotExistingId() {
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> updatePersonUseCase.execute(999L, validPhysicalRecord));
        verify(personRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAttributeAlreadyExistsExceptionWhenCpfExistsOnOtherPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(physicalModel));
        // Mapeia para encontrar OUTRA pessoa com o CPF informado
        when(personRepository.findByCpf(any())).thenReturn(Optional.of(otherPhysicalModel));

        Assertions.assertThrows(AttributeAlreadyExistsException.class, () -> updatePersonUseCase.execute(1L, validPhysicalRecord));
        verify(personRepository, never()).save(any());
    }
}
