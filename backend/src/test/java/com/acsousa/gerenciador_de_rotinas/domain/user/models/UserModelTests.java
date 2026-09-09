package com.acsousa.gerenciador_de_rotinas.domain.user.models;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class UserModelTests {

    @Test
    public void shouldCreateUserModelWhenValidData() {
        UserModel userModel = UserFactory.createUserModel();

        Assertions.assertNotNull(userModel);
        Assertions.assertEquals(1L, userModel.getId());
        Assertions.assertEquals("Hal Jordan", userModel.getName());
        Assertions.assertEquals("hal.jordan@email.com", userModel.getEmail());
        Assertions.assertEquals(EntityStatus.ACTIVE, userModel.getStatus());
    }
}
