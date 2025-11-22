package com.acsousa.gerenciador_de_rotinas.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerGetIT {
    @Autowired
    private MockMvc mockMvc;

    private Long existingUserId;

    @BeforeEach
    void setUp() throws Exception {
        existingUserId = 1L;
    }

    @Test
    public void findByIdShouldReturnEntityFromDatabaseWhenExistingId() throws Exception {
        mockMvc.perform(get("/users/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Bruce Wayne"))
                .andExpect(jsonPath("$.login").value("batman"))
                .andExpect(jsonPath("$.email").value("bruce.wayne@email.com"));
    }
}
