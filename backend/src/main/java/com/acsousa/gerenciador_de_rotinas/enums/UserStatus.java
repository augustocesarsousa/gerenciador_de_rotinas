package com.acsousa.gerenciador_de_rotinas.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}
