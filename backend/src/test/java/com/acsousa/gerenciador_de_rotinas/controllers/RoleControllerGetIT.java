package com.acsousa.gerenciador_de_rotinas.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoleControllerGetIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findAllShouldReturnList() throws Exception {
        mockMvc.perform(get("/users/roles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].authority").value("ROLE_FINANCIAL_MANAGER"))
                .andExpect(jsonPath("$.[1].description").value("Gerente Financeiro"));
    }

}
