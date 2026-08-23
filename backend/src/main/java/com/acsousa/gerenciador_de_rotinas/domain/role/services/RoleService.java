package com.acsousa.gerenciador_de_rotinas.domain.role.services;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.role.usecases.FindAllRolesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final FindAllRolesUseCase findAllRolesUseCase;

    public List<RoleResponseRecord> findAll() {
        return findAllRolesUseCase.execute();
    }
}
