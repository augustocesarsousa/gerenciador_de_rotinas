package com.acsousa.gerenciador_de_rotinas.enums;

import lombok.Getter;

@Getter
public enum UserStatus implements DescribableEnum {
    ACTIVE("Ativo"),
    INACTIVE("Inativo");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription(){
        return this.description;
    }
}
