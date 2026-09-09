package com.acsousa.gerenciador_de_rotinas.domain.user.records;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Representação detalhada do cadastro de um usuário no sistema")
public record UserResponseRecord(
    @Schema(description = "Identificador único do usuário", example = "1")
    Long id,

    @Schema(description = "Nome completo do usuário", example = "Bruce Wayne")
    String name,

    @Schema(description = "Login do usuário", example = "batman")
    String login,

    @Schema(description = "E-mail do usuário", example = "bruce.wayne@email.com")
    String email,

    @Schema(description = "Status do usuário no sistema", example = "ACTIVE")
    EntityStatus status,

    @Schema(description = "Lista de perfis de acesso vinculados ao usuário")
    Set<RoleResponseRecord> roles,

    @Schema(description = "Data e hora de criação do cadastro", example = "2025-01-01T00:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Data e hora da última atualização do cadastro", example = "2025-01-01T00:00:00")
    LocalDateTime updatedAt
) {
    public static UserResponseRecord fromEntity(UserModel entity) {
        if (entity == null) {
            return null;
        }

        Set<RoleResponseRecord> roleRecords = entity.getRoles().stream()
                .map(RoleResponseRecord::fromEntity)
                .collect(Collectors.toSet());

        return new UserResponseRecord(
                entity.getId(),
                entity.getName(),
                entity.getLogin(),
                entity.getEmail(),
                entity.getStatus(),
                roleRecords,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
