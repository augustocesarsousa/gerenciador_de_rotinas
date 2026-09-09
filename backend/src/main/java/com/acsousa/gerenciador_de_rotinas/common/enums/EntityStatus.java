package com.acsousa.gerenciador_de_rotinas.common.enums;

import lombok.Getter;

@Getter
public enum EntityStatus implements DescribableEnum {
    ACTIVE("Ativo"),
    INACTIVE("Inativo");

    private final String description;

    EntityStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
