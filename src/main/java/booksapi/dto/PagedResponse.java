package booksapi.dto;

import org.springframework.hateoas.CollectionModel;

public record PagedResponse<T>(
        CollectionModel<T> data,
        PageMetadata pagination
) {
}
