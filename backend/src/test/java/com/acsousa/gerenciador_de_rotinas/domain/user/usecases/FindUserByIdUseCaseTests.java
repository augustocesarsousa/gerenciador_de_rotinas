package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FindUserByIdUseCaseTests {
    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private UserRepository userRepository;

    private Long existingId;
    private Long notExistingId;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        notExistingId = 1000L;
        userModel = UserFactory.createUserModel();
    }

    @Test
    public void shouldReturnUserWhenIdExists() {
        when(userRepository.findById(existingId)).thenReturn(Optional.of(userModel));

        UserResponseRecord responseRecord = findUserByIdUseCase.execute(existingId);

        Assertions.assertNotNull(responseRecord);
        Assertions.assertEquals(existingId, responseRecord.id());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(userRepository.findById(notExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            findUserByIdUseCase.execute(notExistingId);
        });
    }
}
