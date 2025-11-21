package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.services.UserService;
import com.acsousa.gerenciador_de_rotinas.utils.mapper.ConvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserModel create(UserDTO userDTO) {
        userDTO.setStatus(UserStatus.ACTIVE);
        userDTO.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userDTO.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(ConvertMapper.convertObject(userDTO, UserModel.class));
    }
}
