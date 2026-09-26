package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.specifications.BankAccountQueryFilter;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("Testes de Persistência - BankAccountRepository")
public class BankAccountRepositoryTests {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankRepository bankRepository;

    private BankModel seededBank;

    @BeforeEach
    void setUp() {
        seededBank = bankRepository.findById(1L).orElseGet(() -> {
            BankModel newBank = new BankModel(
                    null, "001", "00000000", "Banco do Brasil S.A.", "Banco do Brasil",
                    EntityStatus.ACTIVE, Instant.now(), Instant.now(), 1L
            );
            return bankRepository.save(newBank);
        });
    }

    @Test
    @DisplayName("Deve persistir e recuperar conta com relacionamento ManyToOne para BankModel")
    void shouldSaveAndFindBankAccountWithBankRelation() {
        // given
        BankAccountModel model = BankAccountFactory.createBankAccountModel(seededBank);
        model.setId(null);

        // when
        BankAccountModel saved = bankAccountRepository.save(model);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBank()).isNotNull();
        assertThat(saved.getBank().getId()).isEqualTo(seededBank.getId());
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve persistir Caixa Interno (CASH_DESK) com bank_id nulo com sucesso")
    void shouldSaveCashDeskAccountWithNullBank() {
        // given
        BankAccountModel cashDesk = BankAccountFactory.createCashDeskModel();
        cashDesk.setId(null);

        // when
        BankAccountModel saved = bankAccountRepository.save(cashDesk);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBank()).isNull();
        assertThat(saved.getAccountType()).isEqualTo(BankAccountType.CASH_DESK);
    }

    @Test
    @DisplayName("Deve retornar true em existsBy... quando combinação banco + agência + conta ativa já existir")
    void shouldReturnTrueWhenActiveAccountExists() {
        // given
        BankAccountModel model = BankAccountFactory.createBankAccountModel(seededBank);
        model.setId(null);
        model.setAgencyNumber("9999");
        model.setAccountNumber("88888");
        model.setStatus(EntityStatus.ACTIVE);
        bankAccountRepository.save(model);

        // when
        boolean exists = bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(
                seededBank.getId(), "9999", "88888", EntityStatus.ACTIVE
        );

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false em existsBy...AndIdNot para o próprio registro")
    void shouldReturnFalseWhenCheckingSameAccountExcludingItsId() {
        // given
        BankAccountModel model = BankAccountFactory.createBankAccountModel(seededBank);
        model.setId(null);
        model.setAgencyNumber("7777");
        model.setAccountNumber("66666");
        model.setStatus(EntityStatus.ACTIVE);
        BankAccountModel saved = bankAccountRepository.save(model);

        // when
        boolean existsForSameId = bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatusAndIdNot(
                seededBank.getId(), "7777", "66666", EntityStatus.ACTIVE, saved.getId()
        );

        // then
        assertThat(existsForSameId).isFalse();
    }

    @Test
    @DisplayName("Deve filtrar contas dinamicamente utilizando BankAccountSpecification")
    void shouldFilterAccountsUsingSpecification() {
        // given
        BankAccountModel model = BankAccountFactory.createBankAccountModel(seededBank);
        model.setId(null);
        model.setDescription("Conta Específica Teste");
        model.setAccountType(BankAccountType.SAVINGS);
        bankAccountRepository.save(model);

        BankAccountQueryFilter filter = new BankAccountQueryFilter();
        filter.setAccountType(BankAccountType.SAVINGS);
        filter.setDescription("Específica");

        // when
        Page<BankAccountModel> result = bankAccountRepository.findAll(filter.toSpecification(), PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getAccountType()).isEqualTo(BankAccountType.SAVINGS);
        assertThat(result.getContent().get(0).getDescription()).contains("Específica");
    }
}
