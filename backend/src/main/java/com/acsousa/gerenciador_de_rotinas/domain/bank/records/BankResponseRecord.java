package com.acsousa.gerenciador_de_rotinas.domain.bank.records;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Representação detalhada dos dados de uma instituição bancária")
public record BankResponseRecord(
    @Schema(description = "Identificador único do banco", example = "1")
    Long id,

    @Schema(description = "Código de compensação COMPE/Febraban", example = "001")
    String code,

    @Schema(description = "Identificador do Sistema de Pagamentos Brasileiro", example = "00000000")
    String ispb,

    @Schema(description = "Razão Social ou Nome Oficial", example = "Banco do Brasil S.A.")
    String name,

    @Schema(description = "Nome fantasia ou abreviado", example = "Banco do Brasil")
    String shortName,

    @Schema(description = "Status do banco no sistema (ACTIVE / INACTIVE)", example = "ACTIVE")
    EntityStatus status,

    @Schema(description = "Data e hora de criação do cadastro", example = "2026-01-01T00:00:00Z")
    Instant createdAt,

    @Schema(description = "Data e hora da última atualização do cadastro", example = "2026-01-01T00:00:00Z")
    Instant updatedAt,

    @Schema(description = "ID do usuário de edição que realizou a última alteração", example = "1")
    Long userIdEdit
) {
    public static BankResponseRecord fromEntity(BankModel entity) {
        if (entity == null) {
            return null;
        }
        return new BankResponseRecord(
            entity.getId(),
            entity.getCode(),
            entity.getIspb(),
            entity.getName(),
            entity.getShortName(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getUserIdEdit()
        );
    }
}
