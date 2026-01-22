package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class RoleServiceImplIT {
    @Autowired
    private RoleServiceImpl roleService;

    @Test
    public void findAllShouldReturnList() {
        List<RoleDTO> roleDTOList = roleService.findAll();

        Assertions.assertNotNull(roleDTOList);
        Assertions.assertFalse(roleDTOList.isEmpty());
    }
}
