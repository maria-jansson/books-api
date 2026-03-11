package booksapi.dto;

/**
 * Data Transfer Object representing an author.
 *
 * @param id the unique identifier of the author
 * @param name the name of the author
 */
public record AuthorDTO(Long id, String name) {}
