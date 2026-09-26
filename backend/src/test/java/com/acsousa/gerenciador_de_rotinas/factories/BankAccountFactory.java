package com.acsousa.gerenciador_de_rotinas.factories;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountUpdateRecord;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class BankAccountFactory {

    public static BankAccountModel createBankAccountModel(BankModel bank) {
        return new BankAccountModel(
                1L,
                bank,
                BankAccountType.CHECKING,
                "Conta Principal - Bradesco",
                "1234",
                "0",
                "123456",
                "7",
                "Edital 2026/01",
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 1, 1),
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );
    }

    public static BankAccountModel createCashDeskModel() {
        return new BankAccountModel(
                2L,
                null,
                BankAccountType.CASH_DESK,
                "Caixa Cantina",
                null,
                null,
                null,
                null,
                null,
                new BigDecimal("200.00"),
                LocalDate.of(2026, 1, 1),
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );
    }

    public static BankAccountCreateRecord createBankAccountCreateRecord(Long bankId) {
        return new BankAccountCreateRecord(
                bankId,
                BankAccountType.CHECKING,
                "Conta Principal - Bradesco",
                "1234",
                "0",
                "123456",
                "7",
                "Edital 2026/01",
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 1, 1),
                EntityStatus.ACTIVE,
                1L
        );
    }

    public static BankAccountCreateRecord createCashDeskCreateRecord() {
        return new BankAccountCreateRecord(
                null,
                BankAccountType.CASH_DESK,
                "Caixa Cantina",
                null,
                null,
                null,
                null,
                null,
                new BigDecimal("200.00"),
                LocalDate.of(2026, 1, 1),
                EntityStatus.ACTIVE,
                1L
        );
    }

    public static BankAccountUpdateRecord createBankAccountUpdateRecord(Long bankId) {
        return new BankAccountUpdateRecord(
                bankId,
                BankAccountType.CHECKING,
                "Conta Principal Atualizada",
                "1234",
                "0",
                "123456",
                "7",
                "Novo Convênio 2026",
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 1, 1),
                EntityStatus.ACTIVE,
                1L
        );
    }

    public static BankAccountResponseRecord createBankAccountResponseRecord(Long id) {
        return new BankAccountResponseRecord(
                id,
                1L,
                "Bradesco",
                "237",
                BankAccountType.CHECKING,
                "Corrente",
                "Conta Principal - Bradesco",
                "1234",
                "0",
                "123456",
                "7",
                "Edital 2026/01",
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 1, 1),
                EntityStatus.ACTIVE,
                Instant.now(),
                Instant.now(),
                1L
        );
    }
}
