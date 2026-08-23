package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindUserByIdUseCase {
    private final UserRepository userRepository;

    public UserResponseRecord execute(Long id) {
        log.debug("Executando busca de usuário por id: {}", id);
        return userRepository.findById(id)
                .map(UserResponseRecord::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
