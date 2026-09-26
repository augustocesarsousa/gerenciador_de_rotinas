package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "Representação detalhada dos dados de uma conta bancária")
public record BankAccountResponseRecord(
    @Schema(description = "Identificador único da conta bancária", example = "1")
    Long id,

    @Schema(description = "ID do banco vinculado", example = "1")
    Long bankId,

    @Schema(description = "Nome ou nome fantasia do banco", example = "Banco do Brasil")
    String bankName,

    @Schema(description = "Código COMPE do banco", example = "001")
    String bankCode,

    @Schema(description = "Tipo de conta", example = "CHECKING")
    BankAccountType accountType,

    @Schema(description = "Descrição amigável do tipo de conta", example = "Corrente")
    String accountTypeDescription,

    @Schema(description = "Descrição da conta", example = "Conta Principal - Bradesco")
    String description,

    @Schema(description = "Número da agência", example = "1234")
    String agencyNumber,

    @Schema(description = "Dígito da agência", example = "0")
    String agencyDigit,

    @Schema(description = "Número da conta", example = "123456")
    String accountNumber,

    @Schema(description = "Dígito da conta", example = "7")
    String accountDigit,

    @Schema(description = "Projeto ou convênio vinculado", example = "Edital FNAS 2026")
    String projectOrAgreement,

    @Schema(description = "Saldo inicial", example = "1500.00")
    BigDecimal initialBalance,

    @Schema(description = "Data-base do saldo inicial", example = "2026-01-01")
    LocalDate initialBalanceDate,

    @Schema(description = "Status da conta no sistema (ACTIVE / INACTIVE)", example = "ACTIVE")
    EntityStatus status,

    @Schema(description = "Data e hora de criação do cadastro", example = "2026-01-01T00:00:00Z")
    Instant createdAt,

    @Schema(description = "Data e hora da última atualização do cadastro", example = "2026-01-01T00:00:00Z")
    Instant updatedAt,

    @Schema(description = "ID do usuário da última edição", example = "1")
    Long userIdEdit
) {
    public static BankAccountResponseRecord fromEntity(BankAccountModel entity) {
        if (entity == null) {
            return null;
        }
        return new BankAccountResponseRecord(
            entity.getId(),
            entity.getBank() != null ? entity.getBank().getId() : null,
            entity.getBank() != null ? entity.getBank().getShortName() : null,
            entity.getBank() != null ? entity.getBank().getCode() : null,
            entity.getAccountType(),
            entity.getAccountType() != null ? entity.getAccountType().getDescription() : null,
            entity.getDescription(),
            entity.getAgencyNumber(),
            entity.getAgencyDigit(),
            entity.getAccountNumber(),
            entity.getAccountDigit(),
            entity.getProjectOrAgreement(),
            entity.getInitialBalance(),
            entity.getInitialBalanceDate(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getUserIdEdit()
        );
    }
}
