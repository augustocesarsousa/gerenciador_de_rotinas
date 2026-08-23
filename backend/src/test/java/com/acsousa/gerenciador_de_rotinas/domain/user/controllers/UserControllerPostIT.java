package com.acsousa.gerenciador_de_rotinas.domain.user.controllers;

import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerPostIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateRecord validUserCreateRecord;

    @BeforeEach
    void setUp() throws Exception {
        validUserCreateRecord = UserFactory.createUserCreateRecord();
    }

    @Test
    public void shouldPersistUserInDatabaseWhenValidData() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validUserCreateRecord);

        mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
