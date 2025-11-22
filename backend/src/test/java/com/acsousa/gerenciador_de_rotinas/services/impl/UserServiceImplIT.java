package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.exceptions.custom.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.specifications.queryFilter.UserQueryFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceImplIT {

    @Autowired
    private UserServiceImpl userService;

    private UserDTO validUserDTO;
    private Long notExistingId;
    private Pageable pageable;
    private UserQueryFilter userQueryFilter;

    @BeforeEach
    void setUp() throws Exception {
        validUserDTO = UserFactory.createValidUserDTO();
        notExistingId = 1000L;
        pageable = PageRequest.of(0,10);
        userQueryFilter = new UserQueryFilter();
    }

    @Test
    public void createShouldPersistEntityInDatabaseWhenValidData() {
        UserDTO userDTOCreated = userService.create(validUserDTO);

        Assertions.assertNotNull(userDTOCreated);
        Assertions.assertNotNull(userDTOCreated.getId());
        Assertions.assertEquals("Hal Jordan", userDTOCreated.getName());
        Assertions.assertEquals(UserStatus.ACTIVE, userDTOCreated.getStatus());
        Assertions.assertNotNull(userDTOCreated.getCreatedAt());
    }

    @Test
    public void findByIdShouldReturnEntityFromDatabaseWhenExistingId() {
        UserDTO userDTOCreated = userService.create(validUserDTO);
        
        UserDTO userDTOFound = userService.findById(userDTOCreated.getId());
        
        Assertions.assertNotNull(userDTOFound);
        Assertions.assertEquals(userDTOFound.getId(), userDTOCreated.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNotExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(notExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPagedResults() {
        Page<UserDTO> userDTOPage = userService.findAll(userQueryFilter.toSpecification(), pageable);

        Assertions.assertNotNull(userDTOPage);
        Assertions.assertFalse(userDTOPage.isEmpty());
    }

    @Test
    public void updateShouldUpdateEntityInDatabaseWhenValidData(){
        UserDTO userDTOCreated = userService.create(validUserDTO);

        userDTOCreated.setName("John Stewart");
        UserDTO userDTOUpdated = userService.update(userDTOCreated.getId(), userDTOCreated);

        Assertions.assertEquals("John Stewart", userDTOUpdated.getName());
        Assertions.assertEquals(userDTOUpdated.getId(), userDTOCreated.getId());
    }
}
