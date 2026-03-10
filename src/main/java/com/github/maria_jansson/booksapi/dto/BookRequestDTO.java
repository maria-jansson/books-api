package com.github.maria_jansson.booksapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record BookRequestDTO(
        @NotBlank(message = "Title is required")
        String title,
        @NotNull(message = "Author IDs are required")
        List<Long> authorIds,
        String description,
        @NotNull(message = "Category IDs are required")
        List<Long> categoryIds,
        @NotBlank(message = "Publisher is required")
        String publisher,
        @Positive(message = "Price must be a positive number")
        Double price,
        String publishMonth,
        @Positive(message = "Publish year must be a positive number")
        int publishYear
) {
}
