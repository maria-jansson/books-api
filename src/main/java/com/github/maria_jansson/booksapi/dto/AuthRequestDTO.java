package com.github.maria_jansson.booksapi.dto;

public record AuthRequestDTO(
        String username,
        String password
) {
}
