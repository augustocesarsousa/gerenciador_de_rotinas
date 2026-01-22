package com.acsousa.gerenciador_de_rotinas.dtos;

import com.acsousa.gerenciador_de_rotinas.utils.json.Views;
import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonView({Views.Create.class, Views.Find.class, Views.Update.class})
    private Long id;

    @JsonView({Views.Find.class})
    private String authority;

    @JsonView({Views.Find.class})
    private String description;
}
