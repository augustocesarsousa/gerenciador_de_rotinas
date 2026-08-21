package com.acsousa.gerenciador_de_rotinas.domain.role.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.role.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FindAllRolesUseCaseTests {
    @InjectMocks
    private FindAllRolesUseCase findAllRolesUseCase;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() throws Exception {
        RoleModel roleModel = RoleFactory.createRoleAdmin();

        when(roleRepository.findAll()).thenReturn(List.of(roleModel));
    }

    @Test
    public void shouldReturnListWhenExecute() {
        List<RoleResponseRecord> roleResponseRecordList = findAllRolesUseCase.execute();

        Assertions.assertNotNull(roleResponseRecordList);
        Assertions.assertFalse(roleResponseRecordList.isEmpty());
        Assertions.assertEquals(1, roleResponseRecordList.size());
        Assertions.assertEquals("ROLE_ADMIN", roleResponseRecordList.get(0).authority());
    }
}
