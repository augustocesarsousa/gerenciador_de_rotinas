package com.acsousa.gerenciador_de_rotinas.dtos;

import com.acsousa.gerenciador_de_rotinas.enums.UserProfile;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.utils.json.Views;
import com.acsousa.gerenciador_de_rotinas.validations.user.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonView({Views.Find.class})
    private Long id;

    @JsonView({Views.Create.class, Views.Find.class, Views.Update.class})
    @NotBlank(message = "Nome é obrigatório", groups = {Views.Create.class, Views.Update.class})
    private String name;

    @JsonView({Views.Create.class, Views.Find.class, Views.Update.class})
    @NotBlank(message = "Login é obrigatório", groups = {Views.Create.class, Views.Update.class})
    @UserLoginCreateValid(message = "Login já cadastrado para outro usuário", groups = {Views.Create.class})
    @UserLoginUpdateValid(message = "Login já cadastrado para outro usuário", groups = {Views.Update.class})
    private String login;

    @JsonView({Views.Create.class, Views.Update.class})
    @NotBlank(message = "Senha é obrigatória", groups = {Views.Create.class, Views.Update.class})
    private String password;

    @JsonView({Views.Create.class, Views.Find.class, Views.Update.class})
    @Email(message = "E-mail inválido", groups = {Views.Create.class, Views.Update.class})
    @UserEmailCreateValid(message = "Email já cadastrado para outro usuário", groups = {Views.Create.class})
    @UserEmailUpdateValid(message = "Email já cadastrado para outro usuário", groups = {Views.Update.class})
    private String email;

    @JsonView({Views.Find.class, Views.Update.class})
    private UserStatus status;

    @JsonView({Views.Create.class, Views.Find.class, Views.Update.class})
    private UserProfile profile;

    @JsonView({Views.Find.class})
    private LocalDateTime createdAt;

    @JsonView({Views.Find.class})
    private LocalDateTime updatedAt;

    @JsonView({Views.Create.class, Views.Find.class, Views.Update.class})
    @UserIdEditValid(message = "Id do usuário de edição inválido", groups = {Views.Create.class, Views.Update.class})
    private Long userIdEdit;
}
