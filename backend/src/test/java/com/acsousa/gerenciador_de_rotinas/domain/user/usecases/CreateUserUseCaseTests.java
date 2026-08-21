package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.domain.role.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
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
public class CreateUserUseCaseTests {
    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserCreateRecord createRecord;
    private UserModel userModel;
    private RoleModel roleModel;

    @BeforeEach
    void setUp() {
        createRecord = UserFactory.createUserCreateRecord();
        userModel = UserFactory.createUserModel();
        roleModel = RoleFactory.createRoleAdmin();

        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleModel));
    }

    @Test
    public void shouldPersistUserWhenValidData() {
        UserResponseRecord responseRecord = createUserUseCase.execute(createRecord);

        Assertions.assertNotNull(responseRecord);
        Assertions.assertNotNull(responseRecord.id());
        Assertions.assertEquals(createRecord.name(), responseRecord.name());
        Assertions.assertEquals(createRecord.login(), responseRecord.login());
    }
}
