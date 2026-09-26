package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para criação de uma nova conta bancária ou caixa interno")
public record BankAccountCreateRecord(
    @Schema(description = "ID do banco vinculado (obrigatório se não for caixa interno)", example = "1")
    Long bankId,

    @Schema(description = "Tipo de conta (CHECKING, SAVINGS, INVESTMENT, CASH_DESK)", example = "CHECKING")
    @NotNull(message = "Tipo de conta é obrigatório")
    BankAccountType accountType,

    @Schema(description = "Descrição ou nome de identificação da conta", example = "Conta Principal - Bradesco")
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    String description,

    @Schema(description = "Número da agência (máx 5 dígitos)", example = "1234")
    @Size(max = 5, message = "Número da agência deve ter no máximo 5 dígitos")
    String agencyNumber,

    @Schema(description = "Dígito da agência (máx 2 caracteres)", example = "0")
    @Size(max = 2, message = "Dígito da agência deve ter no máximo 2 caracteres")
    String agencyDigit,

    @Schema(description = "Número da conta corrente/poupança (máx 12 dígitos)", example = "123456")
    @Size(max = 12, message = "Número da conta deve ter no máximo 12 dígitos")
    String accountNumber,

    @Schema(description = "Dígito verificador da conta (máx 2 caracteres)", example = "7")
    @Size(max = 2, message = "Dígito da conta deve ter no máximo 2 caracteres")
    String accountDigit,

    @Schema(description = "Projeto ou convênio vinculado para prestação de contas", example = "Edital FNAS 2026")
    @Size(max = 150, message = "Vínculo com projeto/convênio deve ter no máximo 150 caracteres")
    String projectOrAgreement,

    @Schema(description = "Saldo inicial de abertura da conta", example = "0.00")
    @NotNull(message = "Saldo inicial é obrigatório")
    BigDecimal initialBalance,

    @Schema(description = "Data-base de início do saldo", example = "2026-01-01")
    @NotNull(message = "Data do saldo inicial é obrigatória")
    LocalDate initialBalanceDate,

    @Schema(description = "Status da conta no sistema (ACTIVE / INACTIVE)", example = "ACTIVE")
    EntityStatus status,

    @Schema(description = "ID do usuário responsável pelo cadastro", example = "1")
    @NotNull(message = "ID do usuário é obrigatório")
    Long userIdEdit
) {
    public BankAccountModel toEntity(BankModel bank) {
        BankAccountModel entity = new BankAccountModel();
        entity.setBank(bank);
        entity.setAccountType(this.accountType);
        entity.setDescription(this.description != null ? this.description.trim() : null);
        entity.setAgencyNumber(this.agencyNumber != null && !this.agencyNumber.isBlank() ? this.agencyNumber.trim() : null);
        entity.setAgencyDigit(this.agencyDigit != null && !this.agencyDigit.isBlank() ? this.agencyDigit.trim() : null);
        entity.setAccountNumber(this.accountNumber != null && !this.accountNumber.isBlank() ? this.accountNumber.trim() : null);
        entity.setAccountDigit(this.accountDigit != null && !this.accountDigit.isBlank() ? this.accountDigit.trim() : null);
        entity.setProjectOrAgreement(this.projectOrAgreement != null && !this.projectOrAgreement.isBlank() ? this.projectOrAgreement.trim() : null);
        entity.setInitialBalance(this.initialBalance != null ? this.initialBalance : BigDecimal.ZERO);
        entity.setInitialBalanceDate(this.initialBalanceDate != null ? this.initialBalanceDate : LocalDate.now());
        entity.setStatus(this.status != null ? this.status : EntityStatus.ACTIVE);
        entity.setUserIdEdit(this.userIdEdit);
        return entity;
    }
}
