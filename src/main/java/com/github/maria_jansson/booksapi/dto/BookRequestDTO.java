package com.github.maria_jansson.booksapi.dto;

import java.util.List;

public record BookRequestDTO(
        String title,
        List<Long> authorIds,
        String description,
        List<Long> categoryIds,
        String publisher,
        Double price,
        String publishMonth,
        int publishYear
) {
}
