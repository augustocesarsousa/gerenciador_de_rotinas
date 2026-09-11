package com.acsousa.gerenciador_de_rotinas.domain.bank.controllers;

import com.acsousa.gerenciador_de_rotinas.common.records.PageResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.records.BankUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bank.services.BankService;
import com.acsousa.gerenciador_de_rotinas.domain.bank.specifications.BankQueryFilter;
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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/banks")
@RequiredArgsConstructor
@Tag(name = "Bancos", description = "Endpoints para gerenciamento cadastral de instituições financeiras")
public class BankController {
    private final BankService bankService;

    @Operation(summary = "Criar banco", description = "Cadastra uma nova instituição bancária com validação de unicidade de código COMPE")
    @ApiResponse(responseCode = "201", description = "Instituição bancária cadastrada com sucesso e id retornado")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody BankCreateRecord createRecord) {
        Long id = bankService.create(createRecord).id();
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @Operation(summary = "Atualizar banco", description = "Edita as informações de uma instituição bancária existente")
    @ApiResponse(responseCode = "200", description = "Instituição bancária atualizada com sucesso e id retornado")
    @ApiResponse(responseCode = "404", description = "Instituição bancária não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody BankUpdateRecord updateRecord) {
        Long updatedId = bankService.update(id, updateRecord).id();
        return ResponseEntity.status(HttpStatus.OK).body(updatedId);
    }

    @Operation(summary = "Buscar banco por ID", description = "Retorna os detalhes do cadastro de uma instituição bancária específica")
    @ApiResponse(responseCode = "200", description = "Dados do banco recuperados com sucesso")
    @ApiResponse(responseCode = "404", description = "Instituição bancária não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<BankResponseRecord> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bankService.findById(id));
    }

    @Operation(summary = "Listar bancos paginado", description = "Retorna uma lista paginada de instituições bancárias aplicando filtros de consulta")
    @ApiResponse(responseCode = "200", description = "Página de instituições bancárias recuperada com sucesso")
    @GetMapping
    public ResponseEntity<PageResponseRecord<BankResponseRecord>> findAll(
            BankQueryFilter filter,
            @PageableDefault(page = 0, size = 10, sort = "code", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BankResponseRecord> page = bankService.findAll(filter, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(PageResponseRecord.fromPage(page));
    }

    @Operation(summary = "Alternar status do banco", description = "Alterna o status da instituição bancária entre ACTIVE e INACTIVE")
    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso")
    @ApiResponse(responseCode = "404", description = "Instituição bancária não encontrada")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BankResponseRecord> toggleStatus(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "userIdEdit", required = false) Long userIdEdit) {
        return ResponseEntity.status(HttpStatus.OK).body(bankService.toggleStatus(id, userIdEdit));
    }

    @Operation(summary = "Excluir banco", description = "Remove uma instituição bancária caso não possua vínculos com outros registros")
    @ApiResponse(responseCode = "204", description = "Instituição bancária removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Instituição bancária não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        bankService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
