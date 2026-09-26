package com.acsousa.gerenciador_de_rotinas.domain.bankaccount.models;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.bank.models.BankModel;
import com.acsousa.gerenciador_de_rotinas.domain.bankaccount.enums.BankAccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "tb_bank_account",
    indexes = {
        @Index(name = "idx_bank_agency_account", columnList = "bank_id, agency_number, account_number")
    }
)
public class BankAccountModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private BankModel bank;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BankAccountType accountType;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String description;

    @Size(max = 5)
    @Column(name = "agency_number", length = 5)
    private String agencyNumber;

    @Size(max = 2)
    @Column(name = "agency_digit", length = 2)
    private String agencyDigit;

    @Size(max = 12)
    @Column(name = "account_number", length = 12)
    private String accountNumber;

    @Size(max = 2)
    @Column(name = "account_digit", length = 2)
    private String accountDigit;

    @Size(max = 150)
    @Column(name = "project_or_agreement", length = 150)
    private String projectOrAgreement;

    @NotNull
    @Column(name = "initial_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal initialBalance;

    @NotNull
    @Column(name = "initial_balance_date", nullable = false)
    private LocalDate initialBalanceDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EntityStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "user_id_edit", nullable = false)
    private Long userIdEdit;
}
