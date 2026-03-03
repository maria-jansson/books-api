package com.github.maria_jansson.booksapi.dto;

import com.github.maria_jansson.booksapi.model.Author;
import com.github.maria_jansson.booksapi.model.Category;

import java.util.List;

public record BookRequestDTO(
        String title,
        List<Author> authors,
        String description,
        List<Category> categories,
        String publisher,
        Double price,
        String publishMonth,
        int publishYear
) {
}
