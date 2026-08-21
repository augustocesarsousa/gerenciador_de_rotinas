package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.domain.role.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponseRecord execute(Long id, UserUpdateRecord updateRecord) {
        log.info("Iniciando a atualização do usuário com id: {}", id);

        UserModel userModelToUpdate = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado"));

        userModelToUpdate.setName(updateRecord.name());
        userModelToUpdate.setLogin(updateRecord.login());

        if (updateRecord.password() != null && !updateRecord.password().trim().isEmpty()) {
            userModelToUpdate.setPassword(updateRecord.password());
        }

        userModelToUpdate.setEmail(updateRecord.email());
        userModelToUpdate.setUserIdEdit(updateRecord.userIdEdit());

        if (updateRecord.status() != null) {
            userModelToUpdate.setStatus(updateRecord.status());
        }

        userModelToUpdate.getRoles().clear();
        if (updateRecord.roles() != null && !updateRecord.roles().isEmpty()) {
            updateRecord.roles().forEach(roleRecord -> {
                RoleModel roleFound = roleRepository.findById(roleRecord.id()).orElseThrow(
                        () -> new ResourceNotFoundException("Role não encontrada: " + roleRecord.id()));
                userModelToUpdate.getRoles().add(roleFound);
            });
        }

        UserModel updatedUser = userRepository.save(userModelToUpdate);
        log.info("Usuário atualizado com sucesso com id: {}", updatedUser.getId());
        return UserResponseRecord.fromEntity(updatedUser);
    }
}
