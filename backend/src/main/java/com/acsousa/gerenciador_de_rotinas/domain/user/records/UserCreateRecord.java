package com.acsousa.gerenciador_de_rotinas.domain.user.records;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.user.validations.UserEmailCreateValid;
import com.acsousa.gerenciador_de_rotinas.domain.user.validations.UserIdEditValid;
import com.acsousa.gerenciador_de_rotinas.domain.user.validations.UserLoginCreateValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(description = "Dados para criação de um novo usuário")
public record UserCreateRecord(
    @Schema(description = "Nome completo do usuário", example = "Bruce Wayne")
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @Schema(description = "Login único do usuário para autenticação", example = "batman")
    @NotBlank(message = "Login é obrigatório")
    @UserLoginCreateValid
    String login,

    @Schema(description = "Senha de acesso do usuário", example = "1234")
    @NotBlank(message = "Senha é obrigatória")
    String password,

    @Schema(description = "Endereço de e-mail único do usuário", example = "bruce.wayne@email.com")
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @UserEmailCreateValid
    String email,

    @Schema(description = "Lista de perfis de acesso associados ao usuário")
    Set<RoleResponseRecord> roles,

    @Schema(description = "ID do usuário administrador que está realizando a criação", example = "1")
    @NotNull(message = "Id do usuário de edição é obrigatório")
    @UserIdEditValid(message = "Id do usuário de edição inválido")
    Long userIdEdit
) {
    public UserModel toEntity() {
        UserModel entity = new UserModel();
        entity.setName(this.name);
        entity.setLogin(this.login);
        entity.setPassword(this.password);
        entity.setEmail(this.email);
        entity.setUserIdEdit(this.userIdEdit);
        entity.setStatus(EntityStatus.ACTIVE);
        return entity;
    }
}
