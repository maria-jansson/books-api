package booksapi.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 */
public class InvalidCredentialsException extends RuntimeException {
  /**
   * Constructs an InvalidCredentialsException with the given message.
   *
   * @param message a description of the error
   */
  public InvalidCredentialsException(String message) {
    super(message);
  }
}
