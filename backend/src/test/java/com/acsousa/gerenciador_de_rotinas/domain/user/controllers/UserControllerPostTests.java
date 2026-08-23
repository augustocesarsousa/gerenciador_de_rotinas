package com.acsousa.gerenciador_de_rotinas.domain.user.controllers;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.services.UserService;
import com.acsousa.gerenciador_de_rotinas.factories.RoleFactory;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerPostTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long notExistingUserId;
    private String existingLogin;
    private String existingEmail;

    @BeforeEach
    void setUp() throws Exception {
        UserModel userModel = UserFactory.createUserModel();
        UserResponseRecord userResponse = UserFactory.createUserResponseRecord();
        Long existingUserId = 1L;
        notExistingUserId = 1000L;
        existingLogin = "teste";
        existingEmail = "teste@email.com";

        when(userService.create(any())).thenReturn(userResponse);

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(userModel));
        when(userRepository.findById(notExistingUserId)).thenReturn(Optional.empty());
        when(userRepository.findByLogin(existingLogin)).thenReturn(userModel);
        when(userRepository.findByEmail(existingEmail)).thenReturn(userModel);
    }

    private UserCreateRecord createWith(String name, String login, String password, String email, Long userIdEdit) {
        return new UserCreateRecord(
                name,
                login,
                password,
                email,
                Set.of(RoleFactory.createRoleAdminResponseRecord()),
                userIdEdit
        );
    }

    @Test
    public void shouldReturnCreatedWhenValidData() throws Exception {
        UserCreateRecord record = UserFactory.createUserCreateRecord();
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenNameIsBlank() throws Exception {
        UserCreateRecord record = createWith("", "lanterna.verde", "1234", "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith(null, "lanterna.verde", "1234", "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith("Hal Jordan", "", "1234", "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith("Hal Jordan", null, "1234", "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith("Hal Jordan", existingLogin, "1234", "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
    public void shouldReturnUnprocessableEntityWhenPasswordIsBlank() throws Exception {
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", "", "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "password"),
                                        hasEntry("message", "Senha é obrigatória")
                                )
                        )
                ));
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenPasswordIsNull() throws Exception {
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", null, "hal.jordan@email.com", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").value(
                        hasItem(
                                allOf(
                                        hasEntry("fieldName", "password"),
                                        hasEntry("message", "Senha é obrigatória")
                                )
                        )
                ));
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenInvalidEmail() throws Exception {
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", "1234", "invalidEmail", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", "1234", "", 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", "1234", null, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", "1234", existingEmail, 1L);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
    public void shouldReturnUnprocessableEntityWhenInvalidUserIdEdit() throws Exception {
        UserCreateRecord record = createWith("Hal Jordan", "lanterna.verde", "1234", "hal.jordan@email.com", notExistingUserId);
        String jsonBody = objectMapper.writeValueAsString(record);

        mockMvc.perform(post("/users")
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
