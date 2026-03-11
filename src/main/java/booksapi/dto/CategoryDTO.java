package booksapi.dto;

/**
 * Data Transfer Object representing a category.
 *
 * @param id the unique identifier of the category
 * @param name the name of the category
 */
public record CategoryDTO(Long id, String name) {}
