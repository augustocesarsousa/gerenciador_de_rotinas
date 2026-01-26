package com.acsousa.gerenciador_de_rotinas.controllers;

import com.acsousa.gerenciador_de_rotinas.dtos.CustomPageDTO;
import com.acsousa.gerenciador_de_rotinas.dtos.UserDTO;
import com.acsousa.gerenciador_de_rotinas.enums.UserStatus;
import com.acsousa.gerenciador_de_rotinas.records.EnumRecord;
import com.acsousa.gerenciador_de_rotinas.services.impl.UserServiceImpl;
import com.acsousa.gerenciador_de_rotinas.specifications.queryFilter.UserQueryFilter;
import com.acsousa.gerenciador_de_rotinas.utils.enums.EnumUtil;
import com.acsousa.gerenciador_de_rotinas.utils.json.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @JsonView(Views.Create.class)
    @PostMapping
    public ResponseEntity<Long> create(@Validated(Views.Create.class)
                                           @RequestBody UserDTO userDTOToCreate) {
        Long id = userService.create(userDTOToCreate).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @JsonView(Views.Find.class)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @JsonView(Views.Find.class)
    @GetMapping
    public ResponseEntity<CustomPageDTO<UserDTO>> findAll(UserQueryFilter filter,
                                                          @PageableDefault(page = 0, size = 10, sort = "id",
                                                               direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDTO> page = userService.findAll(filter.toSpecification(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomPageDTO<>(page));
    }

    @JsonView(Views.Update.class)
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable(value = "id") Long id,
                                          @RequestBody
                                          @Validated(Views.Update.class)
                                          UserDTO userDTOToUpdate) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, userDTOToUpdate).getId());
    }

    @GetMapping("/status")
    public ResponseEntity<List<EnumRecord>> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(EnumUtil.convertEnumToList(UserStatus.class));
    }
}
