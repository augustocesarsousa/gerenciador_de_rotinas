package com.acsousa.gerenciador_de_rotinas.domain.user.controllers;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.services.UserService;
import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
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
import java.util.Set;

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
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingUserId;
    private Long notExistingUserId;
    private String existingLogin;
    private String existingEmail;

    @BeforeEach
    void setUp() throws Exception {
        UserModel userModel = UserFactory.createUserModel();
        UserResponseRecord userResponse = UserFactory.createUserResponseRecord();
        existingUserId = 1L;
        notExistingUserId = 1000L;
        existingLogin = "teste";
        existingEmail = "teste@email.com";

        when(userService.update(eq(existingUserId), any())).thenReturn(userResponse);
        when(userService.update(eq(notExistingUserId), any())).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(userModel));
        when(userRepository.findById(notExistingUserId)).thenReturn(Optional.empty());
        when(userRepository.findByLogin(existingLogin)).thenReturn(new UserModel());
        when(userRepository.findByEmail(existingEmail)).thenReturn(new UserModel());
    }

    private UserUpdateRecord updateWith(String name, String login, String password, String email, EntityStatus status, Long userIdEdit) {
        return new UserUpdateRecord(
                name,
                login,
                password,
                email,
                status,
                Set.of(RoleFactory.createRoleAdminResponseRecord()),
                userIdEdit
        );
    }

    @Test
    public void shouldReturnOkWhenValidData() throws Exception {
        UserUpdateRecord record = UserFactory.createUserUpdateRecord();
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenNameIsBlank() throws Exception {
        UserUpdateRecord record = updateWith("", "lanterna.verde", "1234", "hal.jordan@email.com", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenNameIsNull() throws Exception {
        UserUpdateRecord record = updateWith(null, "lanterna.verde", "1234", "hal.jordan@email.com", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenLoginIsBlank() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", "", "1234", "hal.jordan@email.com", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenLoginIsNull() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", null, "1234", "hal.jordan@email.com", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenLoginAlreadyExisting() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", existingLogin, "1234", "hal.jordan@email.com", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenInvalidEmail() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", "lanterna.verde", "1234", "invalidEmail", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenEmailIsBlank() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", "lanterna.verde", "1234", "", EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenEmailIsNull() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", "lanterna.verde", "1234", null, EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenEmailAlreadyExisting() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", "lanterna.verde", "1234", existingEmail, EntityStatus.ACTIVE, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

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
    public void shouldReturnUnprocessableEntityWhenInvalidStatus() throws Exception {
        UserUpdateRecord record = UserFactory.createUserUpdateRecord();
        String jsonBody = objectMapper.writeValueAsString(record);
        jsonBody = jsonBody.replace("ACTIVE", "INVALID");

        mockMvc.perform(put("/users/{id}", existingUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("O campo 'status' possui um valor inválido."));
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenInvalidUserIdEdit() throws Exception {
        UserUpdateRecord record = updateWith("Hal Jordan", "lanterna.verde", "1234", "hal.jordan@email.com", EntityStatus.ACTIVE, notExistingUserId);
        String jsonBody = objectMapper.writeValueAsString(record);

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
