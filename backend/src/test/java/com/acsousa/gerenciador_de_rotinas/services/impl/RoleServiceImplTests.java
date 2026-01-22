package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;
import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
import com.acsousa.gerenciador_de_rotinas.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RoleServiceImplTests {
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() throws Exception {
        RoleModel roleModel = RoleFactory.createRoleAdmin();

        when(roleRepository.findAll()).thenReturn(List.of(roleModel));
    }

    @Test
    public void findAllShouldReturnList() {
        List<RoleDTO> roleDTOList = roleService.findAll();

        Assertions.assertNotNull(roleDTOList);
    }
}
