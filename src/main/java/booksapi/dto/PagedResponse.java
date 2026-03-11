package booksapi.dto;

import org.springframework.hateoas.CollectionModel;

/**
 * Generic wrapper for paginated API responses.
 *
 * @param <T> the type of the paginated content
 * @param data the paginated content with HATEOAS links
 * @param pagination metadata about the current page
 */
public record PagedResponse<T>(CollectionModel<T> data, PageMetadata pagination) {}
