package com.acsousa.gerenciador_de_rotinas.services;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    UserModel create(UserDTO userDTO);

    UserDTO findById(Long id);

    Page<UserDTO> findAll(Specification<UserModel> spec, Pageable pageable);
}
