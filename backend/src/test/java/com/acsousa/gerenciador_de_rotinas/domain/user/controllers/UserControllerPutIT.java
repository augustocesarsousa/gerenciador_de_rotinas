package com.acsousa.gerenciador_de_rotinas.domain.user.controllers;

import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerPutIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserUpdateRecord validUserUpdateRecord;
    private Long existingUserId;
    private Long notExistingUserId;

    @BeforeEach
    void setUp() throws Exception {
        validUserUpdateRecord = UserFactory.createUserUpdateRecord();
        existingUserId = 1L;
        notExistingUserId = 1000L;
    }

    @Test
    public void shouldUpdateUserInDatabaseWhenValidData() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validUserUpdateRecord);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validUserUpdateRecord);

        mockMvc.perform(put("/users/{id}", notExistingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
