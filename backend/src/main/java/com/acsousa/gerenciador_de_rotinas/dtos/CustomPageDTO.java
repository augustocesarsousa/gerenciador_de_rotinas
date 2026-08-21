package com.acsousa.gerenciador_de_rotinas.dtos;

import com.acsousa.gerenciador_de_rotinas.utils.json.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPageDTO<T> {
    private final int number;
    private final int totalPages;
    private final long totalElements;

    private final List<T> content;

    public CustomPageDTO(Page<T> page) {
        this.number = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.content = page.getContent();
    }

    @JsonView({Views.Find.class})
    public int getNumber() {
        return number;
    }

    @JsonView({Views.Find.class})
    public int getTotalPages() {
        return totalPages;
    }

    @JsonView({Views.Find.class})
    public long getTotalElements() {
        return totalElements;
    }

    @JsonView({Views.Find.class})
    public List<T> getContent() {
        return content;
    }
}
