package com.acsousa.gerenciador_de_rotinas.controllers;

import org.junit.jupiter.api.BeforeEach;
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

    @Test
    public void findAllShouldReturnPagedResults() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    public void findAllShouldReturnPageWhenSortByNameDesc() throws Exception {
        mockMvc.perform(get("/users?sort=name,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").value("Martha Wayne"))
                .andExpect(jsonPath("$.content[1].name").value("Louis Lane"))
                .andExpect(jsonPath("$.content[2].name").value("Lex Luthor"))
                .andExpect(jsonPath("$.content[3].name").value("Clark Kent"))
                .andExpect(jsonPath("$.content[4].name").value("Bruce Wayne"))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    public void findAllShouldReturnPageWhenFilterByStatusAndProfile() throws Exception {
        mockMvc.perform(get("/users?status=INACTIVE&profile=FINANCIAL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").value("Martha Wayne"))
                .andExpect(jsonPath("$.content[1].name").value("Lex Luthor"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}
