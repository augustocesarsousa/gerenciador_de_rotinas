package com.acsousa.gerenciador_de_rotinas.controllers;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;
import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
import com.acsousa.gerenciador_de_rotinas.services.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(RoleController.class)
public class RoleControllerGetTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() throws Exception {
        RoleDTO roleDTO = RoleFactory.createRoleAdminDTO();

        when(roleService.findAll()).thenReturn(List.of(roleDTO));
    }

    @Test
    public void findAllShouldReturnList() throws Exception {
        mockMvc.perform(get("/users/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].authority").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.[0].description").value("Administrador"));
    }

}
