package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.domain.role.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTests {
    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private Long existingId;
    private Long notExistingId;
    private UserUpdateRecord updateRecord;
    private UserModel userModel;
    private RoleModel roleModel;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        notExistingId = 1000L;
        updateRecord = UserFactory.createUserUpdateRecord();
        userModel = UserFactory.createUserModel();
        roleModel = RoleFactory.createRoleAdmin();
    }

    @Test
    public void shouldUpdateUserWhenValidData() {
        when(userRepository.findById(existingId)).thenReturn(Optional.of(userModel));
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleModel));

        UserResponseRecord responseRecord = updateUserUseCase.execute(existingId, updateRecord);

        Assertions.assertNotNull(responseRecord);
        Assertions.assertEquals(existingId, responseRecord.id());
        Assertions.assertEquals(updateRecord.name(), responseRecord.name());
        Assertions.assertEquals(updateRecord.login(), responseRecord.login());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(userRepository.findById(notExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            updateUserUseCase.execute(notExistingId, updateRecord);
        });
    }
}
