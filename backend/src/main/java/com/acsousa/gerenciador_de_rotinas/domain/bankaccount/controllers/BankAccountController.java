package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.controllers;

import com.acsousa.gerenciador_de_rotinas.common.records.PageResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.services.BankAccountService;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications.BankAccountQueryFilter;
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
@RequestMapping("/bank-accounts")
@RequiredArgsConstructor
@Tag(name = "Contas Bancárias", description = "Endpoints para gerenciamento cadastral de contas bancárias e caixas internos")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Operation(summary = "Criar conta bancária", description = "Cadastra uma nova conta bancária ou caixa interno")
    @ApiResponse(responseCode = "201", description = "Conta bancária cadastrada com sucesso e id retornado")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody BankAccountCreateRecord createRecord) {
        Long id = bankAccountService.create(createRecord).id();
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @Operation(summary = "Atualizar conta bancária", description = "Edita as informações de uma conta bancária existente")
    @ApiResponse(responseCode = "200", description = "Conta bancária atualizada com sucesso e id retornado")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody BankAccountUpdateRecord updateRecord) {
        Long updatedId = bankAccountService.update(id, updateRecord).id();
        return ResponseEntity.status(HttpStatus.OK).body(updatedId);
    }

    @Operation(summary = "Buscar conta bancária por ID", description = "Retorna os detalhes de uma conta bancária específica")
    @ApiResponse(responseCode = "200", description = "Dados da conta bancária recuperados com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponseRecord> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountService.findById(id));
    }

    @Operation(summary = "Listar contas bancárias paginado", description = "Retorna uma lista paginada de contas bancárias aplicando filtros de consulta")
    @ApiResponse(responseCode = "200", description = "Página de contas bancárias recuperada com sucesso")
    @GetMapping
    public ResponseEntity<PageResponseRecord<BankAccountResponseRecord>> findAll(
            BankAccountQueryFilter filter,
            @PageableDefault(page = 0, size = 10, sort = "description", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BankAccountResponseRecord> page = bankAccountService.findAll(filter, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(PageResponseRecord.fromPage(page));
    }

    @Operation(summary = "Alternar status da conta bancária", description = "Alterna o status entre ACTIVE e INACTIVE")
    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BankAccountResponseRecord> toggleStatus(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "userIdEdit", required = false) Long userIdEdit) {
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountService.toggleStatus(id, userIdEdit));
    }

    @Operation(summary = "Excluir conta bancária", description = "Remove uma conta bancária caso não possua movimentações vinculadas")
    @ApiResponse(responseCode = "204", description = "Conta bancária removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        bankAccountService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
