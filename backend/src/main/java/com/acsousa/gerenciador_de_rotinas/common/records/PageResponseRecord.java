package com.acsousa.gerenciador_de_rotinas.common.records;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Estrutura de retorno paginado padrão do sistema")
public record PageResponseRecord<T>(
    @Schema(description = "Número da página atual (iniciando em 0)", example = "0")
    int number,

    @Schema(description = "Total de páginas disponíveis", example = "5")
    int totalPages,

    @Schema(description = "Total de elementos correspondentes ao filtro", example = "45")
    long totalElements,

    @Schema(description = "Conteúdo/dados da página atual")
    List<T> content
) {
    public static <T> PageResponseRecord<T> fromPage(Page<T> page) {
        if (page == null) {
            return null;
        }
        return new PageResponseRecord<>(
            page.getNumber(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getContent()
        );
    }
}
