package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceImplTests {

    @Autowired
    private UserServiceImpl userService;

    private UserDTO validUserDTO;

    @BeforeEach
    void setUp() throws Exception {
        validUserDTO = UserFactory.createValidUserDTO();
    }

    @Test
    public void createShouldPersistEntityInDatabaseWhenValidData() {
        UserModel userModel = userService.create(validUserDTO);

        Assertions.assertNotNull(userModel);
        Assertions.assertNotNull(userModel.getId());
        Assertions.assertEquals("Hal Jordan", userModel.getName());
        Assertions.assertEquals(UserStatus.ACTIVE, userModel.getStatus());
        Assertions.assertNotNull(userModel.getCreatedAt());
    }
}
