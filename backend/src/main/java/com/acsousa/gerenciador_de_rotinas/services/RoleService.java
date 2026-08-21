package com.acsousa.gerenciador_de_rotinas.services;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> findAll();
}
