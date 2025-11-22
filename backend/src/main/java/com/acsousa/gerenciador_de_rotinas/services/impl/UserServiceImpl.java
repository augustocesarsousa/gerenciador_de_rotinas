package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.exceptions.custom.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.services.UserService;
import com.acsousa.gerenciador_de_rotinas.utils.mapper.ConvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO create(UserDTO userDTO) {
        userDTO.setStatus(UserStatus.ACTIVE);
        userDTO.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userDTO.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        UserModel userModelCreated = userRepository.save(ConvertMapper.convertObject(userDTO, UserModel.class));

        return ConvertMapper.convertObject(userModelCreated, UserDTO.class);
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<UserModel> userModelOptional = userRepository.findById(id);

        UserModel userFound = userModelOptional.orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrato"));

        return ConvertMapper.convertObject(userFound, UserDTO.class);
    }

    @Override
    public Page<UserDTO> findAll(Specification<UserModel> spec, Pageable pageable) {
        Page<UserModel> userModelPage = userRepository.findAll(spec, pageable);
        return userModelPage.map(userModel -> ConvertMapper.convertObject(userModel, UserDTO.class));
    }
}
