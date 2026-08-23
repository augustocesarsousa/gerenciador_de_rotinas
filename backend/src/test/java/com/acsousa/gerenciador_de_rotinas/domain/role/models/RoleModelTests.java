package com.acsousa.gerenciador_de_rotinas.domain.role.models;

import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class RoleModelTests {

    @Test
    public void shouldCreateRoleModelWhenValidData() {
        RoleModel roleModel = RoleFactory.createRoleAdmin();

        Assertions.assertNotNull(roleModel);
        Assertions.assertEquals(1L, roleModel.getId());
        Assertions.assertEquals("ROLE_ADMIN", roleModel.getAuthority());
        Assertions.assertEquals("Administrador", roleModel.getDescription());
    }
}
