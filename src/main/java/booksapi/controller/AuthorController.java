package booksapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import booksapi.dto.AuthorDTO;
import booksapi.dto.BookDTO;
import booksapi.dto.PageMetadata;
import booksapi.dto.PagedResponse;
import booksapi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing authors.
 * Provides endpoints for retrieving author resources.
 */
@RestController
@RequestMapping(value = "/api/v1/authors", produces = "application/json")
public class AuthorController {
  private final AuthorService authorService;

  /**
   * Constructs an AuthorController with the given AuthorService.
   *
   * @param authorService the service handling author logic
   */
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  /**
   * Returns a single author by ID with HATEOAS links.
   *
   * @param id the ID of the author to retrieve
   * @return the author with self and books links
   */
  @Operation(
      summary = "Get author by ID",
      description = "Returns a single author with HATEOAS links.")
  @GetMapping("/{id}")
  @ApiResponse(responseCode = "200", description = "Returns a single author")
  @ApiResponse(responseCode = "404", description = "Author not found")
  public ResponseEntity<EntityModel<AuthorDTO>> getOneAuthor(@PathVariable Long id) {
    AuthorDTO authorDTO = authorService.getOneAuthor(id);
    EntityModel<AuthorDTO> model = EntityModel.of(authorDTO);
    model.add(linkTo(methodOn(AuthorController.class).getOneAuthor(id)).withSelfRel());
    model.add(
        linkTo(methodOn(AuthorController.class).getAllBooksByAuthor(id, Pageable.unpaged()))
            .withRel("books"));

    return ResponseEntity.ok(model);
  }

  /**
   * Returns a paginated list of authors, optionally filtered by name.
   *
   * @param authorName optional filter for author name
   * @param pageable pagination parameters
   * @return a paginated response containing author models with HATEOAS links
   */
  @Operation(
      summary = "Get all authors",
      description = "Returns a paginated list of authors. Filter by authorName.")
  @GetMapping
  @ApiResponse(responseCode = "200", description = "Returns paginated list of authors")
  public ResponseEntity<PagedResponse<EntityModel<AuthorDTO>>> getAllAuthors(
      @RequestParam Optional<String> authorName, @Parameter(hidden = true) Pageable pageable) {
    Page<AuthorDTO> authors = authorService.getAllAuthors(authorName, pageable);
    List<EntityModel<AuthorDTO>> authorModels = new ArrayList<>();

    for (AuthorDTO author : authors) {
      EntityModel<AuthorDTO> model = EntityModel.of(author);
      model.add(linkTo(methodOn(AuthorController.class).getOneAuthor(author.id())).withSelfRel());
      authorModels.add(model);
    }

    // Build self link from actual request URL to avoid HATEOAS generating a URL template
    String selfLink = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

    CollectionModel<EntityModel<AuthorDTO>> collectionModel =
        CollectionModel.of(authorModels, Link.of(selfLink).withSelfRel());

    PageMetadata pageMetadata =
        new PageMetadata(
            authors.getNumber(),
            authors.getSize(),
            authors.getTotalElements(),
            authors.getTotalPages());

    return ResponseEntity.ok(new PagedResponse<>(collectionModel, pageMetadata));
  }

  /**
   * Returns a paginated list of books written by the specified author.
   *
   * @param id the ID of the author
   * @param pageable pagination parameters
   * @return a paginated response containing book models with HATEOAS links
   */
  @Operation(
      summary = "Get books by author",
      description = "Returns a paginated list of books written by the specified author.")
  @GetMapping("/{id}/books")
  @ApiResponse(responseCode = "200", description = "Returns books by author")
  @ApiResponse(responseCode = "404", description = "Author not found")
  public ResponseEntity<PagedResponse<EntityModel<BookDTO>>> getAllBooksByAuthor(
      @PathVariable Long id, @Parameter(hidden = true) Pageable pageable) {
    Page<BookDTO> books = authorService.getBooksByAuthor(id, pageable);
    List<EntityModel<BookDTO>> bookModels = new ArrayList<>();

    for (BookDTO book : books) {
      EntityModel<BookDTO> model = EntityModel.of(book);
      model.add(linkTo(methodOn(BookController.class).getOneBook(book.id())).withSelfRel());
      bookModels.add(model);
    }

    // Build self link from actual request URL to avoid HATEOAS generating a URL template
    String selfLink = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

    CollectionModel<EntityModel<BookDTO>> collectionModel =
        CollectionModel.of(bookModels, Link.of(selfLink).withSelfRel());

    PageMetadata pageMetadata =
        new PageMetadata(
            books.getNumber(), books.getSize(), books.getTotalElements(), books.getTotalPages());

    return ResponseEntity.ok(new PagedResponse<>(collectionModel, pageMetadata));
  }
}
