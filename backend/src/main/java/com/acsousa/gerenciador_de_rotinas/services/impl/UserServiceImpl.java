package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;
import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.services.UserService;
import com.acsousa.gerenciador_de_rotinas.common.utils.ConvertMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDTO create(UserDTO userDTOToCreate) {
        UserModel userModelToCreate = ConvertMapper.convertObject(userDTOToCreate, UserModel.class);

        if (userDTOToCreate.getRoles() != null && !userDTOToCreate.getRoles().isEmpty()) {
            userModelToCreate.getRoles().clear();

            userDTOToCreate.getRoles().forEach(roleDTO -> {
                RoleModel roleFound = roleRepository.findById(roleDTO.getId()).orElseThrow(
                        () -> new ResourceNotFoundException("Role não encontrada: " + roleDTO.getId()));

                userModelToCreate.getRoles().add(roleFound);
            });
        }

        UserModel userModelCreated = userRepository.save(userModelToCreate);

        return convertUserModelToUserDTOWithRoles(userModelCreated);
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<UserModel> userModelOptional = userRepository.findById(id);
        UserModel userModelFound = userModelOptional.orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrato"));

        return convertUserModelToUserDTOWithRoles(userModelFound);
    }

    @Override
    public Page<UserDTO> findAll(Specification<UserModel> spec, Pageable pageable) {
        Page<UserModel> userModelPage = userRepository.findAll(spec, pageable);

        return userModelPage.map(this::convertUserModelToUserDTOWithRoles);
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTOToUpdate) {
        UserModel userModelToUpdate = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrato"));

        userDTOToUpdate.setId(userModelToUpdate.getId());
        if(userDTOToUpdate.getPassword() == null || userDTOToUpdate.getPassword().trim().isEmpty()) {
            userDTOToUpdate.setPassword(userModelToUpdate.getPassword());
        }

        DozerBeanMapperBuilder.buildDefault().map(userDTOToUpdate, userModelToUpdate);

        if (userDTOToUpdate.getRoles() != null && !userDTOToUpdate.getRoles().isEmpty()) {
            userModelToUpdate.getRoles().clear();

            userDTOToUpdate.getRoles().forEach(roleDTO -> {
                RoleModel roleFound = roleRepository.findById(roleDTO.getId()).orElseThrow(
                        () -> new ResourceNotFoundException("Role não encontrada: " + roleDTO.getId()));

                userModelToUpdate.getRoles().add(roleFound);
                userModelToUpdate.setUpdatedAt(LocalDateTime.now());
            });
        }

        UserModel userModelUpdated = userRepository.save(userModelToUpdate);

        return ConvertMapper.convertObject(userModelUpdated, UserDTO.class);
    }

    private UserDTO convertUserModelToUserDTOWithRoles(UserModel userModel) {
        UserDTO userDto = ConvertMapper.convertObject(userModel, UserDTO.class);

        Set<RoleDTO> rolesDto = userModel.getRoles().stream().map(roleModel -> {
            RoleDTO rd = new RoleDTO();
            rd.setId(roleModel.getId());
            rd.setAuthority(roleModel.getAuthority());
            rd.setDescription(roleModel.getDescription());
            return rd;
        }).collect(Collectors.toSet());

        userDto.setRoles(rolesDto);

        return userDto;
    }
}
