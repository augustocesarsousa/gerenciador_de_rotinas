package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountModel, Long>, JpaSpecificationExecutor<BankAccountModel> {
    boolean existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(
        Long bankId, String agencyNumber, String accountNumber, EntityStatus status
    );

    boolean existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
        Long bankId, String agencyNumber, String accountNumber, EntityStatus status, Long id
    );

    boolean existsByBankId(Long bankId);
}
