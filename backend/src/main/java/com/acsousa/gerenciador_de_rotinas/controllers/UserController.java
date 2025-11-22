package com.acsousa.gerenciador_de_rotinas.controllers;

import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.services.impl.UserServiceImpl;
import com.acsousa.gerenciador_de_rotinas.utils.mapper.ConvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        UserModel userModel = userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertMapper.convertObject(userModel, UserDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }
}
