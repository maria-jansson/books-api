package booksapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import booksapi.dto.AuthorDTO;
import booksapi.dto.BookDTO;
import booksapi.dto.BookRequestDTO;
import booksapi.dto.PageMetadata;
import booksapi.dto.PagedResponse;
import booksapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing books.
 * Provides endpoints for CRUD operations on book resources.
 */
@RestController
@RequestMapping(value = "/api/v1/books", produces = "application/json")
public class BookController {
  private final BookService bookService;

  /**
   * Constructs a BookController with the given BookService.
   *
   * @param bookService the service handling book logic
   */
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  /**
   * Returns a paginated list of books, optionally filtered by author or category name.
   *
   * @param authorName optional filter for author name
   * @param categoryName optional filter for category name
   * @param pageable pagination parameters
   * @return a paginated response containing book models with HATEOAS links
   */
  @Operation(summary = "Get all books",
          description = "Returns a paginated list of books. Filter by authorName or categoryName.")
  @GetMapping
  @ApiResponse(responseCode = "200", description = "Returns paginated list of books")
  public ResponseEntity<PagedResponse<EntityModel<BookDTO>>> getAllBooks(
          @RequestParam Optional<String> authorName,
          @RequestParam Optional<String> categoryName,
          @Parameter(hidden = true) Pageable pageable) {
    Page<BookDTO> books = bookService.getAllBooks(authorName, categoryName, pageable);
    List<EntityModel<BookDTO>> bookModels = new ArrayList<>();

    for (BookDTO book : books) {
      EntityModel<BookDTO> model = EntityModel.of(book);
      model.add(linkTo(methodOn(BookController.class).getOneBook(book.id())).withSelfRel());
      bookModels.add(model);
    }

    // Build self link from actual request URL to avoid HATEOAS generating a URL template
    String selfLink = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .toUriString();

    CollectionModel<EntityModel<BookDTO>> collectionModel = CollectionModel.of(
            bookModels,
            Link.of(selfLink).withSelfRel()
    );

    PageMetadata pageMetadata = new PageMetadata(
            books.getNumber(),
            books.getSize(),
            books.getTotalElements(),
            books.getTotalPages()
    );

    return ResponseEntity.ok(new PagedResponse<>(collectionModel, pageMetadata));
  }

  /**
   * Returns a single book by ID with HATEOAS links.
   *
   * @param id the ID of the book to retrieve
   * @return the book with self, update, delete and author links
   */
  @Operation(summary = "Get book by ID",
          description = "Returns a single book with HATEOAS links.")
  @GetMapping("/{id}")
  @ApiResponse(responseCode = "200", description = "Returns a single book")
  @ApiResponse(responseCode = "404", description = "Book not found")
  public ResponseEntity<EntityModel<BookDTO>> getOneBook(@PathVariable Long id) {
    BookDTO bookDTO = bookService.getOneBook(id);
    EntityModel<BookDTO> model = EntityModel.of(bookDTO);
    addLinksToModel(id, model);
    return ResponseEntity.ok(model);
  }

  /**
   * Creates a new book and returns it with HATEOAS links.
   *
   * @param data the request body containing book details
   * @return the created book with its location header set
   */
  @Operation(summary = "Create a book",
          description = "Creates a new book. Requires JWT authentication.")
  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping
  @ApiResponse(responseCode = "201", description = "Book created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid request body")
  @ApiResponse(responseCode = "401", description = "Authentication required")
  public ResponseEntity<EntityModel<BookDTO>> createBook(@Valid @RequestBody BookRequestDTO data) {
    BookDTO createdBook = bookService.createBook(data);
    EntityModel<BookDTO> model = EntityModel.of(createdBook);
    Long id = createdBook.id();
    addLinksToModel(id, model);

    // Create and set resource location URL
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    return ResponseEntity.created(location).body(model);
  }

  /**
   * Updates an existing book by ID and returns the updated book.
   *
   * @param id the ID of the book to update
   * @param data the request body containing updated book details
   * @return the updated book with HATEOAS links
   */
  @Operation(summary = "Update a book",
          description = "Updates an existing book by ID. Requires JWT authentication.")
  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping("/{id}")
  @ApiResponse(responseCode = "200", description = "Book updated successfully")
  @ApiResponse(responseCode = "400", description = "Invalid request body")
  @ApiResponse(responseCode = "401", description = "Authentication required")
  @ApiResponse(responseCode = "404", description = "Book not found")
  public ResponseEntity<EntityModel<BookDTO>> updateBook(
          @PathVariable Long id,
          @Valid @RequestBody BookRequestDTO data) {
    BookDTO updatedBook = bookService.updateBook(id, data);
    EntityModel<BookDTO> model = EntityModel.of(updatedBook);
    addLinksToModel(id, model);
    return ResponseEntity.ok(model);
  }

  /**
   * Deletes a book by ID.
   *
   * @param id the ID of the book to delete
   * @return an empty response with status 204
   */
  @Operation(summary = "Delete a book",
          description = "Deletes a book by ID. Requires JWT authentication.")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/{id}")
  @ApiResponse(responseCode = "204", description = "Book deleted successfully")
  @ApiResponse(responseCode = "401", description = "Authentication required")
  @ApiResponse(responseCode = "404", description = "Book not found")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Adds HATEOAS links to a book model, including self, update, delete and author links.
   *
   * @param id the ID of the book
   * @param model the EntityModel to add links to
   */
  private void addLinksToModel(Long id, EntityModel<BookDTO> model) {
    model.add(linkTo(methodOn(BookController.class)
            .getOneBook(id)).withSelfRel().withType("GET"));
    model.add(linkTo(methodOn(BookController.class)
            .updateBook(id, null)).withRel("update").withType("PUT"));
    model.add(linkTo(methodOn(BookController.class)
            .deleteBook(id)).withRel("delete").withType("DELETE"));

    BookDTO book = model.getContent();
    if (book.authors() != null) {
      for (AuthorDTO author : book.authors()) {
        model.add(linkTo(methodOn(AuthorController.class)
                .getOneAuthor(author.id())).withRel("author"));
      }
    }
  }
}