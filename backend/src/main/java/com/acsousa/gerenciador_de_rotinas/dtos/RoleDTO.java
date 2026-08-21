package com.acsousa.gerenciador_de_rotinas.dtos;

import lombok.*;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String authority;
    private String description;

    @Override
    public String toString() {
        return "RoleModel{id=" + id + ", authority='" + authority + "'}";
    }
}
