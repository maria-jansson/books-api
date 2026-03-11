package booksapi.dto;

public record AuthRequestDTO(
        String username,
        String password
) {
}
