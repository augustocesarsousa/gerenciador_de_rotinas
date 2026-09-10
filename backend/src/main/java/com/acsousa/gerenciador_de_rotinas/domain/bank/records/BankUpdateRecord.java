package com.acsousa.gerenciador_de_rotinas.domain.bank.records;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de uma instituição bancária existente")
public record BankUpdateRecord(
    @Schema(description = "Código de compensação COMPE/Febraban (exatamente 3 dígitos)", example = "001")
    @NotBlank(message = "Código COMPE é obrigatório")
    @Pattern(regexp = "^\\d{3}$", message = "Código COMPE deve conter exatamente 3 dígitos numéricos")
    String code,

    @Schema(description = "Identificador do Sistema de Pagamentos Brasileiro (8 dígitos)", example = "00000000")
    @Pattern(regexp = "^(\\d{8})?$", message = "ISPB deve conter exatamente 8 dígitos numéricos")
    String ispb,

    @Schema(description = "Razão Social ou Nome Oficial do Banco", example = "Banco do Brasil S.A.")
    @NotBlank(message = "Razão Social é obrigatória")
    @Size(min = 3, max = 150, message = "Razão Social deve ter entre 3 e 150 caracteres")
    String name,

    @Schema(description = "Nome fantasia ou abreviado", example = "Banco do Brasil")
    @NotBlank(message = "Nome abreviado é obrigatório")
    @Size(min = 2, max = 60, message = "Nome abreviado deve ter entre 2 e 60 caracteres")
    String shortName,

    @Schema(description = "Status do banco no sistema (ACTIVE / INACTIVE)", example = "ACTIVE")
    @NotNull(message = "Status é obrigatório")
    EntityStatus status,

    @Schema(description = "ID do usuário de edição que realiza a alteração", example = "1")
    @NotNull(message = "ID do usuário de edição é obrigatório")
    Long userIdEdit
) {
    public void updateEntity(BankModel entity) {
        entity.setCode(this.code != null ? this.code.trim() : null);
        entity.setIspb(this.ispb != null && !this.ispb.isBlank() ? this.ispb.trim() : null);
        entity.setName(this.name != null ? this.name.trim() : null);
        entity.setShortName(this.shortName != null ? this.shortName.trim() : null);
        entity.setStatus(this.status);
        entity.setUserIdEdit(this.userIdEdit);
    }
}
