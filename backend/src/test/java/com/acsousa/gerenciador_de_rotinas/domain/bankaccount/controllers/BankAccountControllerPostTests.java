package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.controllers;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BankAccountController.class)
@DisplayName("Testes de API - POST /bank-accounts")
public class BankAccountControllerPostTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BankAccountService bankAccountService;

    @Test
    @DisplayName("Deve retornar HTTP 201 (Created) com o ID numérico quando payload for válido")
    void shouldReturnCreatedWhenPayloadIsValid() throws Exception {
        // given
        BankAccountCreateRecord createRecord = BankAccountFactory.createBankAccountCreateRecord(1L);
        BankAccountResponseRecord responseRecord = BankAccountFactory.createBankAccountResponseRecord(10L);
        when(bankAccountService.create(any())).thenReturn(responseRecord);

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecord)))
                .andExpect(status().isCreated())
                .andExpect(content().string("10"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando descrição for vazia (Bean Validation)")
    void shouldReturnUnprocessableEntityWhenDescriptionIsBlank() throws Exception {
        // given
        BankAccountCreateRecord invalidRecord = new BankAccountCreateRecord(
                1L, BankAccountType.CHECKING, "   ", "1234", "0", "123456", "7", null,
                new BigDecimal("100.00"), LocalDate.now(), null, 1L
        );

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRecord)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Validation exception"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando agência ultrapassar 5 dígitos (Bean Validation)")
    void shouldReturnUnprocessableEntityWhenAgencyExceedsLimit() throws Exception {
        // given
        BankAccountCreateRecord invalidRecord = new BankAccountCreateRecord(
                1L, BankAccountType.CHECKING, "Conta Teste", "123456", "0", "123456", "7", null,
                new BigDecimal("100.00"), LocalDate.now(), null, 1L
        );

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRecord)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando conta ultrapassar 12 dígitos (Bean Validation)")
    void shouldReturnUnprocessableEntityWhenAccountNumberExceedsLimit() throws Exception {
        // given
        BankAccountCreateRecord invalidRecord = new BankAccountCreateRecord(
                1L, BankAccountType.CHECKING, "Conta Teste", "1234", "0", "1234567890123", "7", null,
                new BigDecimal("100.00"), LocalDate.now(), null, 1L
        );

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRecord)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando saldo inicial ou data forem nulos (Bean Validation)")
    void shouldReturnUnprocessableEntityWhenInitialBalanceOrDateIsNull() throws Exception {
        // given
        BankAccountCreateRecord invalidRecord = new BankAccountCreateRecord(
                1L, BankAccountType.CHECKING, "Conta Teste", "1234", "0", "123456", "7", null,
                null, null, null, 1L
        );

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRecord)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar HTTP 409 (Conflict) quando service lançar AttributeAlreadyExistsException")
    void shouldReturnConflictWhenAccountAlreadyExists() throws Exception {
        // given
        BankAccountCreateRecord createRecord = BankAccountFactory.createBankAccountCreateRecord(1L);
        when(bankAccountService.create(any()))
                .thenThrow(new AttributeAlreadyExistsException("Já existe uma conta bancária ativa cadastrada"));

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecord)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe uma conta bancária ativa cadastrada"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 422 quando service lançar BusinessValidationException")
    void shouldReturnUnprocessableEntityWhenBusinessRuleFails() throws Exception {
        // given
        BankAccountCreateRecord createRecord = BankAccountFactory.createBankAccountCreateRecord(1L);
        when(bankAccountService.create(any()))
                .thenThrow(new BusinessValidationException("A instituição bancária é obrigatória"));

        // when & then
        mockMvc.perform(post("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecord)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("A instituição bancária é obrigatória"));
    }
}
