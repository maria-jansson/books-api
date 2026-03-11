package booksapi.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {
  /**
   * Constructs a ResourceNotFoundException with the given message.
   *
   * @param message a description of the error
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
