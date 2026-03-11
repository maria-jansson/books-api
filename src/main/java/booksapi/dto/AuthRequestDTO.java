package booksapi.dto;

/**
 * Data Transfer Object for authentication requests.
 *
 * @param username the username of the user
 * @param password the password of the user
 */
public record AuthRequestDTO(String username, String password) {}
