package com.acsousa.gerenciador_de_rotinas.factories;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserStatus;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;

import java.util.Set;

public class UserFactory {

    public static UserModel createUserModel() {
        UserModel userModel = new UserModel();

        userModel.setId(1L);
        userModel.setName("Hal Jordan");
        userModel.setLogin("lanterna.verde");
        userModel.setPassword("1234");
        userModel.setEmail("hal.jordan@email.com");
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.getRoles().add(RoleFactory.createRoleAdmin());
        userModel.setUserIdEdit(1L);

        return userModel;
    }

    public static UserCreateRecord createUserCreateRecord() {
        return new UserCreateRecord(
                "Hal Jordan",
                "lanterna.verde",
                "1234",
                "hal.jordan@email.com",
                Set.of(RoleFactory.createRoleAdminResponseRecord()),
                1L
        );
    }

    public static UserUpdateRecord createUserUpdateRecord() {
        return new UserUpdateRecord(
                "Hal Jordan",
                "lanterna.verde",
                "1234",
                "hal.jordan@email.com",
                UserStatus.ACTIVE,
                Set.of(RoleFactory.createRoleAdminResponseRecord()),
                1L
        );
    }

    public static UserResponseRecord createUserResponseRecord() {
        return UserResponseRecord.fromEntity(createUserModel());
    }
}
