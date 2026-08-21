package com.acsousa.gerenciador_de_rotinas.controllers;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerPutTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private Long existingUserId;
    private Long notExistingUserId;
    private String existingLogin;
    private String existingEmail;
    private String blankUserFiled;
    private String nullUserField;

    @BeforeEach
    void setUp() throws Exception {
        UserModel userModel = UserFactory.createUserModel();
        userDTO = UserFactory.createValidUserDTO();
        userDTO.setStatus(UserStatus.ACTIVE);
        existingUserId = 1L;
        notExistingUserId = 1000L;
        existingLogin = "teste";
        existingEmail = "teste@email.com";
        blankUserFiled = "";
        nullUserField = null;

        when(userService.update(eq(existingUserId), any())).thenReturn(userDTO);
        when(userService.update(eq(notExistingUserId), any())).thenThrow(ResourceNotFoundException.class);

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(userModel));
        when(userRepository.findById(notExistingUserId)).thenReturn(Optional.empty());
        when(userRepository.findByLogin(existingLogin)).thenReturn(new UserModel());
        when(userRepository.findByEmail(existingEmail)).thenReturn(new UserModel());
    }

    @Test
    public void updateShouldReturnOkWhenValidData() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenNameIsBlank() throws Exception {
        userDTO.setName(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "name"),
                                        hasEntry("message", "Nome é obrigatório")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenNameIsNull() throws Exception {
        userDTO.setName(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "name"),
                                        hasEntry("message", "Nome é obrigatório")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenLoginIsBlank() throws Exception {
        userDTO.setLogin(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "login"),
                                        hasEntry("message", "Login é obrigatório")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenLoginIsNull() throws Exception {
        userDTO.setLogin(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "login"),
                                        hasEntry("message", "Login é obrigatório")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenLoginAlreadyExisting() throws Exception {
        userDTO.setLogin(existingLogin);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "login"),
                                        hasEntry("message", "Login já cadastrado para outro usuário")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenInvalidEmail() throws Exception {
        userDTO.setEmail("invalidEmail");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "email"),
                                        hasEntry("message", "E-mail inválido")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenEmailIsBlank() throws Exception {
        userDTO.setEmail(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "email"),
                                        hasEntry("message", "E-mail é obrigatório")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenEmailIsNull() throws Exception {
        userDTO.setEmail(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "email"),
                                        hasEntry("message", "E-mail é obrigatório")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenEmailAlreadyExisting() throws Exception {
        userDTO.setEmail(existingEmail);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "email"),
                                        hasEntry("message", "E-mail já cadastrado para outro usuário")
                                )
                        )
                ));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenInvalidStatus() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);
        jsonBody = jsonBody.replace("ACTIVE", "INVALID");

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("O campo 'status' possui um valor inválido."));
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenInvalidUserIdEdit() throws Exception {
        userDTO.setUserIdEdit(notExistingUserId);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "userIdEdit"),
                                        hasEntry("message", "Usuário de edição não encontrado")
                                )
                        )
                ));
    }
}
