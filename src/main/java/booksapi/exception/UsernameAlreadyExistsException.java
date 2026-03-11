package booksapi.exception;

/**
 * Exception thrown when a username is already taken during registration.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
  /**
   * Constructs a UsernameAlreadyExistsException with the given message.
   *
   * @param message a description of the error
   */
  public UsernameAlreadyExistsException(String message) {
    super(message);
  }
}
