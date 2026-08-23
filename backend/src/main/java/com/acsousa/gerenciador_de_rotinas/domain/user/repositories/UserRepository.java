package com.acsousa.gerenciador_de_rotinas.domain.user.repositories;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {
    UserModel findByLogin(String login);

    UserModel findByEmail(String email);
}
