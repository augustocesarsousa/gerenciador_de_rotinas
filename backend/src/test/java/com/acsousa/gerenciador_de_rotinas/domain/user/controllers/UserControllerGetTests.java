package com.acsousa.gerenciador_de_rotinas.domain.user.controllers;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.services.UserService;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerGetTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private Long existingUserId;
    private Long notExistingUserId;

    @BeforeEach
    void setUp() throws Exception {
        existingUserId = 1L;
        notExistingUserId = 1000L;
        UserResponseRecord userResponse = UserFactory.createUserResponseRecord();
        PageImpl<UserResponseRecord> page = new PageImpl<>(List.of(userResponse));

        when(userService.findById(existingUserId)).thenReturn(userResponse);
        when(userService.findById(notExistingUserId)).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));
        when(userService.findAll(any(), any())).thenReturn(page);
    }

    @Test
    public void shouldReturnUserResponseRecordWhenIdExists() throws Exception {
        mockMvc.perform(get("/users/{id}", existingUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.login").exists());
    }

    @Test
    public void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/{id}", notExistingUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnPagedResultsWhenFindAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists());
    }
}
