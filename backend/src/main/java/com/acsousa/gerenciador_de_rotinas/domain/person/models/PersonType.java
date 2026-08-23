package com.acsousa.gerenciador_de_rotinas.domain.person.models;

import com.acsousa.gerenciador_de_rotinas.common.enums.DescribableEnum;
import lombok.Getter;

@Getter
public enum PersonType implements DescribableEnum {
    PHYSICAL("Pessoa Física"),
    LEGAL("Pessoa Jurídica");

    private final String description;

    PersonType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
