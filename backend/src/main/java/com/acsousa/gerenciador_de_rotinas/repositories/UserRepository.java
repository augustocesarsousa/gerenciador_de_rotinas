package com.acsousa.gerenciador_de_rotinas.repositories;

import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
