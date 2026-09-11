package com.acsousa.gerenciador_de_rotinas.domain.bank.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bank.specifications.BankQueryFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FindAllBanksUseCase {
    private final BankRepository bankRepository;

    @Transactional(readOnly = true)
    public Page<BankResponseRecord> execute(BankQueryFilter filter, Pageable pageable) {
        Specification<BankModel> spec = filter != null ? filter.toSpecification() : null;
        Page<BankModel> page = bankRepository.findAll(spec, pageable);
        return page.map(BankResponseRecord::fromEntity);
    }
}
