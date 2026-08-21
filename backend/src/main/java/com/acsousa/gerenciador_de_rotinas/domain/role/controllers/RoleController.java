package com.acsousa.gerenciador_de_rotinas.domain.role.controllers;

import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.role.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Endpoints para gerenciamento de perfis de acesso")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Listar perfis de acesso", description = "Retorna uma lista simples com todos os perfis disponíveis no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfis de acesso recuperada com sucesso")
    @GetMapping
    public ResponseEntity<List<RoleResponseRecord>> findAll() {
        List<RoleResponseRecord> roleResponseList = roleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(roleResponseList);
    }
}
