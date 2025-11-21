package com.acsousa.gerenciador_de_rotinas.services;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;

public interface UserService {
    UserModel create(UserDTO userDTO);
}
