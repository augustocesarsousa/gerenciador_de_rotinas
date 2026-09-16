package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.controllers;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.services.BankAccountService;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BankAccountController.class)
@DisplayName("Testes de API - PUT /bank-accounts/{id}")
public class BankAccountControllerPutTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BankAccountService bankAccountService;

    @Test
    @DisplayName("Deve retornar HTTP 200 (OK) e ID numérico quando atualização for válida")
    void shouldReturnOkWhenUpdateIsValid() throws Exception {
        // given
        BankAccountUpdateRecord updateRecord = BankAccountFactory.createBankAccountUpdateRecord(1L);
        BankAccountResponseRecord responseRecord = BankAccountFactory.createBankAccountResponseRecord(1L);
        when(bankAccountService.update(eq(1L), any())).thenReturn(responseRecord);

        // when & then
        mockMvc.perform(put("/bank-accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 404 (Not Found) quando ID informado não existir")
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        // given
        BankAccountUpdateRecord updateRecord = BankAccountFactory.createBankAccountUpdateRecord(1L);
        when(bankAccountService.update(eq(999L), any()))
                .thenThrow(new ResourceNotFoundException("Conta bancária não encontrada com o ID: 999"));

        // when & then
        mockMvc.perform(put("/bank-accounts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecord)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Conta bancária não encontrada com o ID: 999"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 409 (Conflict) quando atualização violar unicidade de outra conta ativa")
    void shouldReturnConflictWhenDataConflictsWithOtherAccount() throws Exception {
        // given
        BankAccountUpdateRecord updateRecord = BankAccountFactory.createBankAccountUpdateRecord(1L);
        when(bankAccountService.update(eq(1L), any()))
                .thenThrow(new AttributeAlreadyExistsException("Já existe outra conta bancária ativa cadastrada"));

        // when & then
        mockMvc.perform(put("/bank-accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecord)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe outra conta bancária ativa cadastrada"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando falhar validação de regra de negócio")
    void shouldReturnUnprocessableEntityWhenBusinessRuleFails() throws Exception {
        // given
        BankAccountUpdateRecord updateRecord = BankAccountFactory.createBankAccountUpdateRecord(1L);
        when(bankAccountService.update(eq(1L), any()))
                .thenThrow(new BusinessValidationException("A instituição bancária é obrigatória"));

        // when & then
        mockMvc.perform(put("/bank-accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRecord)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("A instituição bancária é obrigatória"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando campo status for nulo (Bean Validation)")
    void shouldReturnUnprocessableEntityWhenStatusIsNull() throws Exception {
        // given
        BankAccountUpdateRecord invalidRecord = new BankAccountUpdateRecord(
                1L, BankAccountType.CHECKING, "Conta", "1234", "0", "123456", "7", null,
                null, null, null, 1L
        );

        // when & then
        mockMvc.perform(put("/bank-accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRecord)))
                .andExpect(status().isUnprocessableEntity());
    }
}
