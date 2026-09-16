package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.controllers;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.services.BankAccountService;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BankAccountController.class)
@DisplayName("Testes de API - GET /bank-accounts")
public class BankAccountControllerGetTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankAccountService bankAccountService;

    @Test
    @DisplayName("Deve retornar HTTP 200 (OK) e dados da conta quando ID existir")
    void shouldReturnOkWhenIdExists() throws Exception {
        // given
        BankAccountResponseRecord response = BankAccountFactory.createBankAccountResponseRecord(1L);
        when(bankAccountService.findById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/bank-accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Conta Principal - Bradesco"))
                .andExpect(jsonPath("$.bankName").value("Bradesco"))
                .andExpect(jsonPath("$.bankCode").value("237"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 404 (Not Found) quando ID não existir")
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        // given
        when(bankAccountService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Conta bancária não encontrada com o ID: 999"));

        // when & then
        mockMvc.perform(get("/bank-accounts/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Conta bancária não encontrada com o ID: 999"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 200 (OK) com página de contas e metadados de paginação")
    void shouldReturnPagedListWhenQueried() throws Exception {
        // given
        BankAccountResponseRecord response = BankAccountFactory.createBankAccountResponseRecord(1L);
        when(bankAccountService.findAll(any(), any())).thenReturn(new PageImpl<>(List.of(response)));

        // when & then
        mockMvc.perform(get("/bank-accounts")
                        .param("page", "0")
                        .param("size", "10")
                        .param("accountType", "CHECKING")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Conta Principal - Bradesco"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
