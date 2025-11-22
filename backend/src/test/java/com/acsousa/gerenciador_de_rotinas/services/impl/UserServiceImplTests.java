package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.exceptions.custom.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceImplTests {

    @Autowired
    private UserServiceImpl userService;

    private UserDTO validUserDTO;
    private Long notExistingId;

    @BeforeEach
    void setUp() throws Exception {
        validUserDTO = UserFactory.createValidUserDTO();
        notExistingId = 1000L;
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

    @Test
    public void findByIdShouldReturnEntityFromDatabaseWhenExistingId() {
        UserModel userModelCreated = userService.create(validUserDTO);
        
        UserDTO userModelFound = userService.findById(userModelCreated.getId());
        
        Assertions.assertNotNull(userModelFound);
        Assertions.assertEquals(userModelFound.getId(), userModelCreated.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNotExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(notExistingId);
        });
    }
}
