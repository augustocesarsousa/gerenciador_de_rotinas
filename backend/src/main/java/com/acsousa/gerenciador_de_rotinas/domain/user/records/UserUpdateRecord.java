package com.acsousa.gerenciador_de_rotinas.domain.user.records;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserStatus;
import com.acsousa.gerenciador_de_rotinas.domain.user.validations.UserEmailUpdateValid;
import com.acsousa.gerenciador_de_rotinas.domain.user.validations.UserIdEditValid;
import com.acsousa.gerenciador_de_rotinas.domain.user.validations.UserLoginUpdateValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(description = "Dados para atualização cadastral de um usuário")
public record UserUpdateRecord(
    @Schema(description = "Nome completo do usuário", example = "Bruce Wayne")
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @Schema(description = "Login único do usuário para autenticação", example = "batman")
    @NotBlank(message = "Login é obrigatório")
    @UserLoginUpdateValid
    String login,

    @Schema(description = "Senha de acesso do usuário (deixe nula ou vazia para manter a senha atual)", example = "1234")
    String password,

    @Schema(description = "Endereço de e-mail único do usuário", example = "bruce.wayne@email.com")
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @UserEmailUpdateValid
    String email,

    @Schema(description = "Status atual do usuário no sistema", example = "ACTIVE")
    UserStatus status,

    @Schema(description = "Lista de perfis de acesso associados ao usuário")
    Set<RoleResponseRecord> roles,

    @Schema(description = "ID do usuário administrador que está realizando a edição", example = "1")
    @NotNull(message = "Id do usuário de edição é obrigatório")
    @UserIdEditValid(message = "Id do usuário de edição inválido")
    Long userIdEdit
) {
}
