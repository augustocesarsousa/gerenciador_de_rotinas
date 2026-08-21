package com.acsousa.gerenciador_de_rotinas.repositories;

import com.acsousa.gerenciador_de_rotinas.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
}
