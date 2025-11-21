package com.acsousa.gerenciador_de_rotinas.factories;

import com.acsousa.gerenciador_de_rotinas.enums.UserProfile;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;

import java.time.LocalDateTime;

public class UserFactory {

    public static UserModel createUserModel() {
        UserModel userModel = new UserModel();

        userModel.setId(1L);
        userModel.setName("Hal Jordan");
        userModel.setLogin("lanterna.verde");
        userModel.setPassword("1234");
        userModel.setEmail("hal.jordan@email.com");
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setProfile(UserProfile.ADMIN);
        userModel.setCreatedAt(LocalDateTime.of(2025,1,1,9,30));
        userModel.setUpdatedAt(LocalDateTime.of(2025,1,1,9,30));
        userModel.setIdUserEdit(1L);

        return userModel;
    }
}
