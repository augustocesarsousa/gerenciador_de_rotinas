package com.acsousa.gerenciador_de_rotinas.domain.person.repositories;

import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Long>, JpaSpecificationExecutor<PersonModel> {
    Optional<PersonModel> findByCpf(String cpf);
    Optional<PersonModel> findByCnpj(String cnpj);
}
