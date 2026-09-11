package com.acsousa.gerenciador_de_rotinas.domain.bank.repositories;

import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankModel, Long>, JpaSpecificationExecutor<BankModel> {
    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    Optional<BankModel> findByCode(String code);
}
