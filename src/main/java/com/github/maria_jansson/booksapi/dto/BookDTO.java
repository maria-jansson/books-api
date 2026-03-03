package com.github.maria_jansson.booksapi.dto;

import java.util.List;

public record BookDTO(
        Long id,
        String title,
        List<AuthorDTO> authors,
        String description,
        List<CategoryDTO> categories,
        String publisher,
        Double price,
        String publishMonth,
        int publishYear) {
}
