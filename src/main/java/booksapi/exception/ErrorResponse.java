package booksapi.exception;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing an API error response.
 *
 * @param status the HTTP status code
 * @param message a description of the error
 * @param timestamp the time the error occurred
 */
public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
