package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.specifications.UserQueryFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllUsersUseCase {
    private final UserRepository userRepository;

    public Page<UserResponseRecord> execute(UserQueryFilter queryFilter, Pageable pageable) {
        log.debug("Executando listagem paginada de usuários com filtros");
        return userRepository.findAll(queryFilter.toSpecification(), pageable)
                .map(UserResponseRecord::fromEntity);
    }
}
