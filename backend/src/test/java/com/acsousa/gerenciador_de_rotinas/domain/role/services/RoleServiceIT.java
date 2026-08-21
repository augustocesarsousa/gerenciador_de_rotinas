package com.acsousa.gerenciador_de_rotinas.domain.role.services;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
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
public class RoleServiceIT {
    @Autowired
    private RoleService roleService;

    @Test
    public void shouldReturnListWhenFindAll() {
        List<RoleResponseRecord> roleResponseList = roleService.findAll();

        Assertions.assertNotNull(roleResponseList);
        Assertions.assertFalse(roleResponseList.isEmpty());
    }
}
