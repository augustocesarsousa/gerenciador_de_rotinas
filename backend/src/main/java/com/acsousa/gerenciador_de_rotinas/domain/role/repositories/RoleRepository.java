package com.acsousa.gerenciador_de_rotinas.domain.role.repositories;

import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
}
