package com.acsousa.gerenciador_de_rotinas.domain.person.records;

import com.acsousa.gerenciador_de_rotinas.common.enums.EntityStatus;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonModel;
import com.acsousa.gerenciador_de_rotinas.domain.person.models.PersonType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação de uma nova pessoa")
public record PersonCreateRecord(
    @Schema(description = "Nome ou Razão Social", example = "Bruce Wayne")
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @Schema(description = "Tipo de pessoa (PHYSICAL / LEGAL)", example = "PHYSICAL")
    @NotNull(message = "Tipo de pessoa é obrigatório")
    PersonType type,

    @Schema(description = "Status da pessoa no sistema (ACTIVE / INACTIVE)", example = "ACTIVE")
    @NotNull(message = "Status é obrigatório")
    EntityStatus status,

    @Schema(description = "CPF do indivíduo (obrigatório apenas para PHYSICAL)", example = "12345678909")
    String cpf,

    @Schema(description = "CNPJ da empresa (obrigatório apenas para LEGAL)", example = "12345678000199")
    String cnpj,

    @Schema(description = "Logradouro/Endereço", example = "Alameda das Flores")
    String address,

    @Schema(description = "Número da residência/empresa", example = "123")
    @PositiveOrZero(message = "Número não pode ser negativo")
    Long number,

    @Schema(description = "Bairro", example = "Centro")
    String neighborhood,

    @Schema(description = "Cidade", example = "Gotham")
    String city,

    @Schema(description = "Estado/UF", example = "SP")
    String state,

    @Schema(description = "CEP (apenas números ou no formato 00000-000)", example = "01001-000")
    @Pattern(regexp = "^(\\d{5}-\\d{3}|\\d{8})?$", message = "CEP inválido")
    String zipcode,

    @Schema(description = "Telefone com DDD (apenas números com 10 ou 11 dígitos)", example = "11999998888")
    @Pattern(regexp = "^(\\d{10,11})?$", message = "Telefone deve conter 10 ou 11 dígitos numéricos")
    String phone,

    @Schema(description = "Endereço de e-mail", example = "contato@empresa.com")
    @Email(message = "E-mail inválido")
    String email,

    @Schema(description = "ID do usuário editor que realiza a ação", example = "1")
    @NotNull(message = "ID do usuário de edição é obrigatório")
    Long userIdEdit
) {
    public PersonModel toEntity() {
        PersonModel entity = new PersonModel();
        entity.setName(this.name);
        entity.setType(this.type);
        entity.setStatus(this.status);
        entity.setCpf(this.cpf);
        entity.setCnpj(this.cnpj);
        entity.setAddress(this.address);
        entity.setNumber(this.number);
        entity.setNeighborhood(this.neighborhood);
        entity.setCity(this.city);
        entity.setState(this.state);
        entity.setZipcode(this.zipcode);
        entity.setPhone(this.phone);
        entity.setEmail(this.email);
        entity.setUserIdEdit(this.userIdEdit);
        return entity;
    }
}
