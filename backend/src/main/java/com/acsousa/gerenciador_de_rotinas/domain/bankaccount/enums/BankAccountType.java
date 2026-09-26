package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums;

import com.acsousa.gerenciador_de_rotinas.common.enums.DescribableEnum;
import lombok.Getter;

@Getter
public enum BankAccountType implements DescribableEnum {
    CHECKING("Corrente"),
    SAVINGS("Poupança"),
    INVESTMENT("Aplicação"),
    CASH_DESK("Caixa Interno");

    private final String description;

    BankAccountType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
