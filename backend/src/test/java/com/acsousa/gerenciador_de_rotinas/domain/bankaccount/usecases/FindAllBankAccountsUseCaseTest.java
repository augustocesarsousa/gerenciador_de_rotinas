package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications.BankAccountQueryFilter;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - FindAllBankAccountsUseCase")
class FindAllBankAccountsUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private FindAllBankAccountsUseCase findAllBankAccountsUseCase;

    @Test
    @DisplayName("Deve retornar página de BankAccountResponseRecord aplicando filtros e paginação")
    void shouldReturnPagedAccountsWhenFilterAndPageableProvided() {
        // given
        BankModel bank = new BankModel();
        bank.setId(1L);
        bank.setShortName("Bradesco");
        bank.setCode("237");

        BankAccountModel model = BankAccountFactory.createBankAccountModel(bank);
        Page<BankAccountModel> pagedModels = new PageImpl<>(List.of(model));

        when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedModels);

        BankAccountQueryFilter filter = new BankAccountQueryFilter();
        filter.setAccountType(BankAccountType.CHECKING);
        filter.setStatus(EntityStatus.ACTIVE);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BankAccountResponseRecord> responsePage = findAllBankAccountsUseCase.execute(filter, pageable);

        // then
        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).id()).isEqualTo(1L);
        assertThat(responsePage.getContent().get(0).description()).isEqualTo("Conta Principal - Bradesco");
        verify(bankAccountRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
