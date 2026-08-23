package com.acsousa.gerenciador_de_rotinas.domain.role.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.role.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllRolesUseCase {
    private final RoleRepository roleRepository;

    public List<RoleResponseRecord> execute() {
        log.debug("Executando caso de uso de listagem de todos os perfis de acesso");
        return roleRepository.findAll()
                .stream()
                .map(RoleResponseRecord::fromEntity)
                .collect(Collectors.toList());
    }
}
