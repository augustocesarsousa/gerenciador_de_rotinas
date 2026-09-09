package com.acsousa.gerenciador_de_rotinas.domain.person.controllers;

import com.acsousa.gerenciador_de_rotinas.common.records.EnumRecord;
import com.acsousa.gerenciador_de_rotinas.common.records.PageResponseRecord;
import com.acsousa.gerenciador_de_rotinas.common.utils.EnumUtil;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.records.PersonUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.person.services.PersonService;
import com.acsousa.gerenciador_de_rotinas.domain.person.specifications.PersonQueryFilter;
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
@RequestMapping("/persons")
@RequiredArgsConstructor
@Tag(name = "Pessoas", description = "Endpoints para gerenciamento cadastral de pessoas (clientes, fornecedores, etc.)")
public class PersonController {
    private final PersonService personService;

    @Operation(summary = "Criar pessoa", description = "Cadastra uma nova pessoa (Física ou Jurídica) com as respectivas validações de documentos")
    @ApiResponse(responseCode = "201", description = "Pessoa cadastrada com sucesso e id retornado")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody PersonCreateRecord createRecord) {
        Long id = personService.create(createRecord).id();
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @Operation(summary = "Atualizar pessoa", description = "Edita as informações de uma pessoa existente")
    @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso e id retornado")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody PersonUpdateRecord updateRecord) {
        Long updatedId = personService.update(id, updateRecord).id();
        return ResponseEntity.status(HttpStatus.OK).body(updatedId);
    }

    @Operation(summary = "Buscar pessoa por ID", description = "Retorna os detalhes do cadastro de uma pessoa específica")
    @ApiResponse(responseCode = "200", description = "Dados da pessoa recuperados com sucesso")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseRecord> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findById(id));
    }

    @Operation(summary = "Listar pessoas paginado", description = "Retorna uma lista paginada de pessoas aplicando filtros de consulta")
    @ApiResponse(responseCode = "200", description = "Página de pessoas recuperada com sucesso")
    @GetMapping
    public ResponseEntity<PageResponseRecord<PersonResponseRecord>> findAll(
            PersonQueryFilter filter,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PersonResponseRecord> page = personService.findAll(filter, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(PageResponseRecord.fromPage(page));
    }

    @Operation(summary = "Listar tipos possíveis de pessoa", description = "Retorna a lista de tipos de pessoas com chaves e descrições formatadas")
    @ApiResponse(responseCode = "200", description = "Lista de tipos retornada com sucesso")
    @GetMapping("/types")
    public ResponseEntity<List<EnumRecord>> getTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(EnumUtil.convertEnumToList(PersonType.class));
    }
}
