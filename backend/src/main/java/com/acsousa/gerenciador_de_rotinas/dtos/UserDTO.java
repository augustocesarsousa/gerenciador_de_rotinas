package com.acsousa.gerenciador_de_rotinas.dtos;

import com.acsousa.gerenciador_de_rotinas.enums.UserProfile;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private UserStatus status;
    private UserProfile profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userIdEdit;
}
