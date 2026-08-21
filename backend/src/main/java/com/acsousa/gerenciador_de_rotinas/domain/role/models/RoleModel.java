package com.acsousa.gerenciador_de_rotinas.domain.role.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_role")
public class RoleModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String authority;

    @Column(nullable = false, unique = true)
    private String description;

    @Override
    public String toString() {
        return "RoleModel{id=" + id + ", authority='" + authority + "'}";
    }
}
