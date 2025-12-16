package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.exceptions.custom.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.specifications.queryFilter.UserQueryFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserServiceImplTests {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private UserDTO userValidDTO;
    private Long existingId;
    private Long notExistingId;
    private UserQueryFilter userQueryFilter;

    @BeforeEach
    void setUp() throws Exception {
        UserModel userModel = UserFactory.createUserModel();
        userValidDTO = UserFactory.createValidUserDTO();
        existingId = 1L;
        notExistingId = 1000L;
        userQueryFilter = new UserQueryFilter();
        PageImpl<UserModel> userModelPage = new PageImpl<>(List.of(userModel));

        when(userRepository.save(any())).thenReturn(userModel);
        when(userRepository.findById(existingId)).thenReturn(Optional.of(userModel));
        when(userRepository.findAll((Specification<UserModel>) any(), (Pageable) any())).thenReturn(userModelPage);
    }

    @Test
    public void createShouldPersistEntityWhenValidData() {
        UserDTO userDTOCreated = userService.create(userValidDTO);

        Assertions.assertNotNull(userDTOCreated.getId());
    }

    @Test
    public void findByIdShouldReturnEntityWhenExistingId() {
        UserDTO userDTOFound = userService.findById(existingId);

        Assertions.assertNotNull(userDTOFound);
        Assertions.assertEquals(userDTOFound.getId(), existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNotExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(notExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<UserDTO> page = userService.findAll(userQueryFilter.toSpecification(), pageable);

        Assertions.assertNotNull(page);
    }

    @Test
    public void updateShouldPersistEntityWhenValidData() {
        UserDTO userDTOUpdated = userService.update(existingId, userValidDTO);

        Assertions.assertNotNull(userDTOUpdated);
        Assertions.assertEquals(userDTOUpdated.getId(), existingId);
        Assertions.assertEquals(userDTOUpdated.getName(), userValidDTO.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenNotExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.update(notExistingId, userValidDTO);
        });
    }

}
