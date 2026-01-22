package com.acsousa.gerenciador_de_rotinas.controllers;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
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
    private UserServiceImpl userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private Long notExistingUserId;
    private String existingLogin;
    private String existingEmail;
    private String blankUserFiled;
    private String nullUserField;

    @BeforeEach
    void setUp() throws Exception {
        UserModel userModel = UserFactory.createUserModel();
        userDTO = UserFactory.createValidUserDTO();
        Long existingUserId = 1L;
        notExistingUserId = 1000L;
        existingLogin = "teste";
        existingEmail = "teste@email.com";
        blankUserFiled = "";
        nullUserField = null;

        when(userService.create(any())).thenReturn(userDTO);

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(userModel));
        when(userRepository.findById(notExistingUserId)).thenReturn(Optional.empty());
        when(userRepository.findByLogin(existingLogin)).thenReturn(userModel);
        when(userRepository.findByEmail(existingEmail)).thenReturn(userModel);
    }

    @Test
    public void createShouldReturnCreatedWhenValidData() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/users")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createShouldReturnUnprocessableEntityWhenNameIsBlank() throws Exception {
        userDTO.setName(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenNameIsNull() throws Exception {
        userDTO.setName(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenLoginIsBlank() throws Exception {
        userDTO.setLogin(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenLoginIsNull() throws Exception {
        userDTO.setLogin(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenLoginAlreadyExisting() throws Exception {
        userDTO.setLogin(existingLogin);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenPasswordIsBlank() throws Exception {
        userDTO.setPassword(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenPasswordIsNull() throws Exception {
        userDTO.setPassword(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenInvalidEmail() throws Exception {
        userDTO.setEmail("invalidEmail");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenEmailIsBlank() throws Exception {
        userDTO.setEmail(blankUserFiled);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenEmailIsNull() throws Exception {
        userDTO.setEmail(nullUserField);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenEmailAlreadyExisting() throws Exception {
        userDTO.setEmail(existingEmail);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
    public void createShouldReturnUnprocessableEntityWhenInvalidUserIdEdit() throws Exception {
        userDTO.setUserIdEdit(notExistingUserId);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

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
