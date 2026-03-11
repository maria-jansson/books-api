package booksapi.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Catches exceptions thrown by controllers and returns consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  /**
   * Handles resource not found exceptions.
   *
   * @param exception the thrown exception
   * @return a 404 error response
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException exception) {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse error = new ErrorResponse(404, exception.getMessage(), timestamp);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Handles username already exists exceptions.
   *
   * @param exception the thrown exception
   * @return a 409 error response
   */
  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(
      UsernameAlreadyExistsException exception) {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse error = new ErrorResponse(409, exception.getMessage(), timestamp);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  /**
   * Handles invalid credentials exceptions.
   *
   * @param exception the thrown exception
   * @return a 401 error response
   */
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
      InvalidCredentialsException exception) {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse error = new ErrorResponse(401, exception.getMessage(), timestamp);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  /**
   * Handles unreadable HTTP message exceptions.
   *
   * @return a 400 error response
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse error = new ErrorResponse(400, "Invalid or missing request body", timestamp);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handles validation exceptions from invalid request bodies.
   *
   * @param exception the thrown exception containing field errors
   * @return a 400 error response with details about the invalid fields
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException exception) {
    LocalDateTime timestamp = LocalDateTime.now();
    String message =
        exception.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
    ErrorResponse error = new ErrorResponse(400, message, timestamp);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handles all uncaught exceptions as a fallback.
   *
   * @return a 500 error response
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse error = new ErrorResponse(500, "An unexpected error occurred", timestamp);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
