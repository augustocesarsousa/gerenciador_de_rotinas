package com.acsousa.gerenciador_de_rotinas.controllers;

import com.acsousa.gerenciador_de_rotinas.dtos.RoleDTO;
import com.acsousa.gerenciador_de_rotinas.services.impl.RoleServiceImpl;
import com.acsousa.gerenciador_de_rotinas.utils.json.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users/roles")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @JsonView(Views.Find.class)
    @GetMapping
    public ResponseEntity<List<RoleDTO>> findAll() {
        List<RoleDTO> roleDTOList = roleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(roleDTOList);
    }

}
