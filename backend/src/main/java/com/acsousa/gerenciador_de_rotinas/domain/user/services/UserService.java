package com.acsousa.gerenciador_de_rotinas.domain.user.services;

import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.specifications.UserQueryFilter;
import com.acsousa.gerenciador_de_rotinas.domain.user.usecases.CreateUserUseCase;
import com.acsousa.gerenciador_de_rotinas.domain.user.usecases.FindAllUsersUseCase;
import com.acsousa.gerenciador_de_rotinas.domain.user.usecases.FindUserByIdUseCase;
import com.acsousa.gerenciador_de_rotinas.domain.user.usecases.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    public UserResponseRecord create(UserCreateRecord createRecord) {
        return createUserUseCase.execute(createRecord);
    }

    public UserResponseRecord findById(Long id) {
        return findUserByIdUseCase.execute(id);
    }

    public Page<UserResponseRecord> findAll(UserQueryFilter queryFilter, Pageable pageable) {
        return findAllUsersUseCase.execute(queryFilter, pageable);
    }

    public UserResponseRecord update(Long id, UserUpdateRecord updateRecord) {
        return updateUserUseCase.execute(id, updateRecord);
    }
}
