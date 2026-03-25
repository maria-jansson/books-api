package booksapi.dto;

/**
 * Data Transfer Object for authentication requests.
 *
 * @param username the username of the user
 * @param password the password of the user
 */
public record AuthRequestDTO(
  @NotBlank(message = "Username is required")
  @Size(max = 50, message = "Username cannot exceed 50 characters")
  String username, 
  @NotBlank(message = "Password is required")
  @Size(max = 100, message = "Password cannot exceed 100 characters")
  String password) {}
