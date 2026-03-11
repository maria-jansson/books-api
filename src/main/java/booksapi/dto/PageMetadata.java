package booksapi.dto;

/**
 * Metadata for paginated responses.
 *
 * @param currentPage the current page number (zero-based)
 * @param pageSize the number of items per page
 * @param totalElements the total number of items across all pages
 * @param totalPages the total number of pages
 */
public record PageMetadata(int currentPage, int pageSize, long totalElements, int totalPages) {}
