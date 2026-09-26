package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.controllers;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.services.BankAccountService;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BankAccountController.class)
@DisplayName("Testes de API - PATCH & DELETE /bank-accounts")
public class BankAccountControllerPatchDeleteTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankAccountService bankAccountService;

    @Test
    @DisplayName("Deve retornar HTTP 200 (OK) ao alternar status com sucesso")
    void shouldReturnOkWhenStatusToggled() throws Exception {
        // given
        BankAccountResponseRecord response = BankAccountFactory.createBankAccountResponseRecord(1L);
        when(bankAccountService.toggleStatus(eq(1L), eq(2L))).thenReturn(response);

        // when & then
        mockMvc.perform(patch("/bank-accounts/1/status")
                        .param("userIdEdit", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve retornar HTTP 404 (Not Found) ao alternar status de ID inexistente")
    void shouldReturnNotFoundWhenTogglingNonExistingId() throws Exception {
        // given
        when(bankAccountService.toggleStatus(eq(999L), eq(1L)))
                .thenThrow(new ResourceNotFoundException("Conta bancária não encontrada com o ID: 999"));

        // when & then
        mockMvc.perform(patch("/bank-accounts/999/status")
                        .param("userIdEdit", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Conta bancária não encontrada com o ID: 999"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 204 (No Content) ao deletar conta com sucesso")
    void shouldReturnNoContentWhenDeleted() throws Exception {
        // given
        doNothing().when(bankAccountService).delete(1L);

        // when & then
        mockMvc.perform(delete("/bank-accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar HTTP 404 (Not Found) ao deletar conta inexistente")
    void shouldReturnNotFoundWhenDeletingNonExistingId() throws Exception {
        // given
        doThrow(new ResourceNotFoundException("Conta bancária não encontrada com o ID: 999"))
                .when(bankAccountService).delete(999L);

        // when & then
        mockMvc.perform(delete("/bank-accounts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Conta bancária não encontrada com o ID: 999"));
    }
}
