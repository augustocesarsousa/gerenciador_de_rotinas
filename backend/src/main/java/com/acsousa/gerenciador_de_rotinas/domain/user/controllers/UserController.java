package com.acsousa.gerenciador_de_rotinas.domain.user.controllers;

import com.acsousa.gerenciador_de_rotinas.common.records.EnumRecord;
import com.acsousa.gerenciador_de_rotinas.common.records.PageResponseRecord;
import com.acsousa.gerenciador_de_rotinas.common.utils.EnumUtil;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserStatus;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.services.UserService;
import com.acsousa.gerenciador_de_rotinas.domain.user.specifications.UserQueryFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento cadastral de usuários")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário no sistema vinculando os perfis correspondentes")
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso e id retornado")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody UserCreateRecord createRecord) {
        Long id = userService.create(createRecord).id();
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes do cadastro de um usuário específico")
    @ApiResponse(responseCode = "200", description = "Dados do usuário recuperados com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseRecord> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @Operation(summary = "Listar usuários paginado", description = "Retorna uma lista paginada de usuários aplicando filtros de consulta")
    @ApiResponse(responseCode = "200", description = "Página de usuários recuperada com sucesso")
    @GetMapping
    public ResponseEntity<PageResponseRecord<UserResponseRecord>> findAll(
            UserQueryFilter filter,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponseRecord> page = userService.findAll(filter, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(PageResponseRecord.fromPage(page));
    }

    @Operation(summary = "Atualizar usuário", description = "Edita as informações de um usuário existente")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso e id retornado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody UserUpdateRecord updateRecord) {
        Long updatedId = userService.update(id, updateRecord).id();
        return ResponseEntity.status(HttpStatus.OK).body(updatedId);
    }

    @Operation(summary = "Listar status possíveis de usuário", description = "Retorna a lista de status de usuários com chaves e descrições formatadas")
    @ApiResponse(responseCode = "200", description = "Lista de status retornada com sucesso")
    @GetMapping("/status")
    public ResponseEntity<List<EnumRecord>> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(EnumUtil.convertEnumToList(UserStatus.class));
    }
}
