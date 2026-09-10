package com.acsousa.gerenciador_de_rotinas.domain.bank.models;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_bank")
public class BankModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^\\d{3}$")
    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Size(max = 8)
    @Column(length = 8)
    private String ispb;

    @NotBlank
    @Size(min = 3, max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Size(min = 2, max = 60)
    @Column(nullable = false, length = 60)
    private String shortName;

    @NotNull
    @Column(nullable = false)
    private EntityStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(nullable = false)
    private Long userIdEdit;
}
