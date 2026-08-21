package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.domain.role.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponseRecord execute(UserCreateRecord createRecord) {
        log.info("Iniciando a criação de um novo usuário: {}", createRecord.login());

        UserModel userModel = createRecord.toEntity();

        if (createRecord.roles() != null && !createRecord.roles().isEmpty()) {
            createRecord.roles().forEach(roleRecord -> {
                RoleModel roleFound = roleRepository.findById(roleRecord.id()).orElseThrow(
                        () -> new ResourceNotFoundException("Role não encontrada: " + roleRecord.id()));
                userModel.getRoles().add(roleFound);
            });
        }

        UserModel savedUser = userRepository.save(userModel);
        log.info("Usuário criado com sucesso com id: {}", savedUser.getId());
        return UserResponseRecord.fromEntity(savedUser);
    }
}
