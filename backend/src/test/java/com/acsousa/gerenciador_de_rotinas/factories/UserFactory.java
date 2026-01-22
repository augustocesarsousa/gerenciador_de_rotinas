package com.acsousa.gerenciador_de_rotinas.factories;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.utils.mapper.ConvertMapper;

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
        userModel.getRoles().add(RoleFactory.createRoleAdmin());
        userModel.setUserIdEdit(1L);

        return userModel;
    }

    public static UserDTO createValidUserDTO() {
        UserDTO userDTO = ConvertMapper.convertObject(createUserModel(), UserDTO.class);

        userDTO.setId(null);
        userDTO.setStatus(null);
        userDTO.setCreatedAt(null);
        userDTO.setUpdatedAt(null);

        return userDTO;
    }
}
