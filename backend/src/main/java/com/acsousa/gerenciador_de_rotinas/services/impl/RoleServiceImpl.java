package com.acsousa.gerenciador_de_rotinas.services.impl;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;
import com.acsousa.gerenciador_de_rotinas.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.repositories.RoleRepository;
import com.acsousa.gerenciador_de_rotinas.services.RoleService;
import com.acsousa.gerenciador_de_rotinas.common.utils.ConvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> findAll() {
        List<RoleModel> roleModelList = roleRepository.findAll();
        return ConvertMapper.convertListOfObjects(roleModelList, RoleDTO.class);
    }
}
