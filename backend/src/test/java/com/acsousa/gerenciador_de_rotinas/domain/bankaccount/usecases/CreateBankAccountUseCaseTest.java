package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.usecases;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.BusinessValidationException;
import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bank.repositories.BankRepository;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models.BankAccountModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.records.BankAccountResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.repositories.BankAccountRepository;
import com.acsousa.gerenciador_de_rotinas.factories.BankAccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - CreateBankAccountUseCase")
class CreateBankAccountUseCaseTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private CreateBankAccountUseCase createBankAccountUseCase;

    private BankModel activeBank;
    private BankAccountCreateRecord validCheckingRecord;
    private BankAccountModel savedCheckingModel;

    @BeforeEach
    void setUp() {
        activeBank = new BankModel(
                1L, "001", "00000000", "Banco do Brasil S.A.", "Banco do Brasil",
                EntityStatus.ACTIVE, Instant.now(), Instant.now(), 1L
        );
        validCheckingRecord = BankAccountFactory.createBankAccountCreateRecord(1L);
        savedCheckingModel = BankAccountFactory.createBankAccountModel(activeBank);
    }

    @Test
    @DisplayName("Deve criar conta bancária tradicional com sucesso quando os dados forem válidos")
    void shouldCreateBankAccountWhenDataIsValid() {
        // given
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(1L, "1234", "123456", EntityStatus.ACTIVE))
                .thenReturn(false);
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenReturn(savedCheckingModel);

        // when
        BankAccountResponseRecord response = createBankAccountUseCase.execute(validCheckingRecord);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.bankId()).isEqualTo(1L);
        assertThat(response.description()).isEqualTo("Conta Principal - Bradesco");
        assertThat(response.accountType()).isEqualTo(BankAccountType.CHECKING);
        assertThat(response.agencyNumber()).isEqualTo("1234");
        assertThat(response.accountNumber()).isEqualTo("123456");

        verify(bankRepository, times(1)).findById(1L);
        verify(bankAccountRepository, times(1)).save(any(BankAccountModel.class));
    }

    @Test
    @DisplayName("Deve criar Caixa Interno (CASH_DESK) com sucesso ignorando e limpando dados bancários")
    void shouldCreateCashDeskAccountWithoutBankDetails() {
        // given
        BankAccountCreateRecord cashDeskRecord = BankAccountFactory.createCashDeskCreateRecord();
        BankAccountModel savedCashDeskModel = BankAccountFactory.createCashDeskModel();
        when(bankAccountRepository.save(any(BankAccountModel.class))).thenReturn(savedCashDeskModel);

        // when
        BankAccountResponseRecord response = createBankAccountUseCase.execute(cashDeskRecord);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.bankId()).isNull();
        assertThat(response.agencyNumber()).isNull();
        assertThat(response.accountNumber()).isNull();
        assertThat(response.accountType()).isEqualTo(BankAccountType.CASH_DESK);

        verify(bankRepository, never()).findById(any());
        verify(bankAccountRepository, times(1)).save(any(BankAccountModel.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessValidationException quando banco for nulo para conta tradicional")
    void shouldThrowBusinessValidationExceptionWhenBankIsNullForTraditionalAccount() {
        // given
        BankAccountCreateRecord recordWithoutBank = new BankAccountCreateRecord(
                null, BankAccountType.CHECKING, "Conta Sem Banco", "1234", "0", "123456", "7",
                null, new BigDecimal("100.00"), LocalDate.now(), EntityStatus.ACTIVE, 1L
        );

        // when & then
        assertThatThrownBy(() -> createBankAccountUseCase.execute(recordWithoutBank))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("A instituição bancária é obrigatória");

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o banco informado não existir")
    void shouldThrowResourceNotFoundExceptionWhenBankNotFound() {
        // given
        when(bankRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> createBankAccountUseCase.execute(validCheckingRecord))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Banco não encontrado");

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessValidationException quando o banco vinculado estiver inativo")
    void shouldThrowBusinessValidationExceptionWhenBankIsInactive() {
        // given
        BankModel inactiveBank = new BankModel(
                2L, "033", "00000000", "Santander", "Santander",
                EntityStatus.INACTIVE, Instant.now(), Instant.now(), 1L
        );
        when(bankRepository.findById(1L)).thenReturn(Optional.of(inactiveBank));

        // when & then
        assertThatThrownBy(() -> createBankAccountUseCase.execute(validCheckingRecord))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("Não é possível vincular uma conta a uma instituição bancária inativa");

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessValidationException quando agência ou conta estiverem vazios")
    void shouldThrowBusinessValidationExceptionWhenAgencyOrAccountIsBlank() {
        // given
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        BankAccountCreateRecord missingAgencyRecord = new BankAccountCreateRecord(
                1L, BankAccountType.CHECKING, "Conta Sem Agência", "", "0", "123456", "7",
                null, new BigDecimal("100.00"), LocalDate.now(), EntityStatus.ACTIVE, 1L
        );

        // when & then
        assertThatThrownBy(() -> createBankAccountUseCase.execute(missingAgencyRecord))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("número da agência é obrigatório");

        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar AttributeAlreadyExistsException quando já existir conta ativa com mesmo banco, agência e conta")
    void shouldThrowAttributeAlreadyExistsExceptionWhenActiveAccountAlreadyExists() {
        // given
        when(bankRepository.findById(1L)).thenReturn(Optional.of(activeBank));
        when(bankAccountRepository.existsByBankIdAndAgencyNumberAndAccountNumberAndStatus(1L, "1234", "123456", EntityStatus.ACTIVE))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> createBankAccountUseCase.execute(validCheckingRecord))
                .isInstanceOf(AttributeAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma conta bancária ativa cadastrada");

        verify(bankAccountRepository, never()).save(any());
    }
}
