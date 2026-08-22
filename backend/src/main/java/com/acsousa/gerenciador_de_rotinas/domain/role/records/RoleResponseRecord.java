package com.acsousa.gerenciador_de_rotinas.domain.role.records;

import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação de retorno dos dados de um perfil de acesso do sistema")
public record RoleResponseRecord(
        @Schema(description = "Identificador único do perfil", example = "1") Long id,

        @Schema(description = "Descrição detalhada do papel/função do perfil", example = "Perfil administrativo com acesso total") String description) {
    public static RoleResponseRecord fromEntity(RoleModel entity) {
        if (entity == null) {
            return null;
        }
        return new RoleResponseRecord(
                entity.getId(),
                entity.getDescription());
    }
}
