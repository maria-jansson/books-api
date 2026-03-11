package booksapi.dto;

import java.util.List;

/**
 * Data Transfer Object representing a book.
 *
 * @param id the unique identifier of the book
 * @param title the title of the book
 * @param authors the list of authors of the book
 * @param description a short description of the book
 * @param categories the list of categories the book belongs to
 * @param publisher the publisher of the book
 * @param price the price of the book
 * @param publishMonth the month the book was published
 * @param publishYear the year the book was published
 */
public record BookDTO(
    Long id,
    String title,
    List<AuthorDTO> authors,
    String description,
    List<CategoryDTO> categories,
    String publisher,
    Double price,
    String publishMonth,
    int publishYear) {}
