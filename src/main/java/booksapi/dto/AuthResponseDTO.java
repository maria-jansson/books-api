package booksapi.dto;

/**
 * Data Transfer Object for authentication responses.
 *
 * @param token the JWT token for the authenticated user
 */
public record AuthResponseDTO(String token) {}
