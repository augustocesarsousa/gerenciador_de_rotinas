package com.acsousa.gerenciador_de_rotinas.enums;

import lombok.Getter;

@Getter
public enum UserProfile {
    ADMIN("Admininstrador"),
    FINANCIAL("Financeiro");

    private final String description;

    UserProfile(String description) {
        this.description = description;
    }
}
