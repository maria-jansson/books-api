package booksapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Data Transfer Object for book creation and update requests.
 *
 * @param title the title of the book
 * @param authorIds the IDs of the authors of the book
 * @param description a short description of the book
 * @param categoryIds the IDs of the categories the book belongs to
 * @param publisher the publisher of the book
 * @param price the price of the book
 * @param publishMonth the month the book was published
 * @param publishYear the year the book was published
 */
public record BookRequestDTO(
        @NotBlank(message = "Title is required")
        String title,
        @NotNull(message = "Author IDs are required")
        List<Long> authorIds,
        String description,
        @NotNull(message = "Category IDs are required")
        List<Long> categoryIds,
        @NotBlank(message = "Publisher is required")
        String publisher,
        @Positive(message = "Price must be a positive number")
        Double price,
        String publishMonth,
        @Positive(message = "Publish year must be a positive number")
        int publishYear
) {
}
