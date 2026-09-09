package com.acsousa.gerenciador_de_rotinas.domain.person.records;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Representação detalhada dos dados de uma pessoa")
public record PersonResponseRecord(
    @Schema(description = "Identificador único da pessoa", example = "1")
    Long id,

    @Schema(description = "Nome ou Razão Social", example = "Bruce Wayne")
    String name,

    @Schema(description = "Tipo de pessoa (PHYSICAL / LEGAL)", example = "PHYSICAL")
    PersonType type,

    @Schema(description = "Status da pessoa no sistema (ACTIVE / INACTIVE)", example = "ACTIVE")
    EntityStatus status,

    @Schema(description = "CPF do indivíduo", example = "12345678909")
    String cpf,

    @Schema(description = "CNPJ da empresa", example = "12345678000199")
    String cnpj,

    @Schema(description = "Logradouro/Endereço", example = "Alameda das Flores")
    String address,

    @Schema(description = "Número da residência/empresa", example = "123")
    Long number,

    @Schema(description = "Bairro", example = "Centro")
    String neighborhood,

    @Schema(description = "Cidade", example = "Gotham")
    String city,

    @Schema(description = "Estado/UF", example = "SP")
    String state,

    @Schema(description = "CEP", example = "01001-000")
    String zipcode,

    @Schema(description = "Telefone com DDD", example = "11999998888")
    String phone,

    @Schema(description = "Endereço de e-mail", example = "contato@empresa.com")
    String email,

    @Schema(description = "Data e hora de criação do cadastro", example = "2026-01-01T00:00:00Z")
    Instant createdAt,

    @Schema(description = "Data e hora da última atualização do cadastro", example = "2026-01-01T00:00:00Z")
    Instant updatedAt,

    @Schema(description = "ID do usuário de edição que realizou a última alteração", example = "1")
    Long userIdEdit
) {
    public static PersonResponseRecord fromEntity(PersonModel entity) {
        if (entity == null) {
            return null;
        }
        return new PersonResponseRecord(
            entity.getId(),
            entity.getName(),
            entity.getType(),
            entity.getStatus(),
            entity.getCpf(),
            entity.getCnpj(),
            entity.getAddress(),
            entity.getNumber(),
            entity.getNeighborhood(),
            entity.getCity(),
            entity.getState(),
            entity.getZipcode(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getUserIdEdit()
        );
    }
}
