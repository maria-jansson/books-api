package booksapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import booksapi.dto.BookDTO;
import booksapi.dto.CategoryDTO;
import booksapi.dto.PageMetadata;
import booksapi.dto.PagedResponse;
import booksapi.service.CategoryService;
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
 * REST controller for managing categories.
 * Provides endpoints for retrieving category resources.
 */
@RestController
@RequestMapping(value = "/api/v1/categories", produces = "application/json")
public class CategoryController {
  private final CategoryService categoryService;

  /**
   * Constructs a CategoryController with the given CategoryService.
   *
   * @param categoryService the service handling category logic
   */
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * Returns a single category by ID with HATEOAS links.
   *
   * @param id the ID of the category to retrieve
   * @return the category with self and books links
   */
  @Operation(summary = "Get category by ID",
          description = "Returns a single category with HATEOAS links.")
  @GetMapping("/{id}")
  @ApiResponse(responseCode = "200", description = "Returns a single category")
  @ApiResponse(responseCode = "404", description = "Category not found")
  public ResponseEntity<EntityModel<CategoryDTO>> getOneCategory(@PathVariable Long id) {
    CategoryDTO categoryDTO = categoryService.getOneCategory(id);
    EntityModel<CategoryDTO> model = EntityModel.of(categoryDTO);
    model.add(linkTo(methodOn(CategoryController.class).getOneCategory(id)).withSelfRel());
    model.add(linkTo(methodOn(CategoryController.class)
            .getAllBooksInCategory(id, Pageable.unpaged())).withRel("books"));

    return ResponseEntity.ok(model);
  }

  /**
   * Returns a paginated list of categories, optionally filtered by name.
   *
   * @param categoryName optional filter for category name
   * @param pageable pagination parameters
   * @return a paginated response containing category models with HATEOAS links
   */
  @Operation(summary = "Get all categories",
          description = "Returns a paginated list of categories. Filter by categoryName.")
  @GetMapping
  @ApiResponse(responseCode = "200", description = "Returns paginated list of categories")
  public ResponseEntity<PagedResponse<EntityModel<CategoryDTO>>> getAllCategories(
          @RequestParam Optional<String> categoryName,
          @Parameter(hidden = true) Pageable pageable) {
    Page<CategoryDTO> categories = categoryService.getAllCategories(categoryName, pageable);
    List<EntityModel<CategoryDTO>> categoryModels = new ArrayList<>();

    for (CategoryDTO category : categories) {
      EntityModel<CategoryDTO> model = EntityModel.of(category);
      model.add(linkTo(methodOn(CategoryController.class)
              .getOneCategory(category.id())).withSelfRel());
      categoryModels.add(model);
    }

    // Build self link from actual request URL to avoid HATEOAS generating a URL template
    String selfLink = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .toUriString();

    CollectionModel<EntityModel<CategoryDTO>> collectionModel = CollectionModel.of(
            categoryModels,
            Link.of(selfLink).withSelfRel()
    );

    PageMetadata pageMetadata = new PageMetadata(
            categories.getNumber(),
            categories.getSize(),
            categories.getTotalElements(),
            categories.getTotalPages()
    );

    return ResponseEntity.ok(new PagedResponse<>(collectionModel, pageMetadata));
  }

  /**
   * Returns a paginated list of books in the specified category.
   *
   * @param id the ID of the category
   * @param pageable pagination parameters
   * @return a paginated response containing book models with HATEOAS links
   */
  @Operation(summary = "Get books in category",
          description = "Returns a paginated list of books in the specified category.")
  @GetMapping("/{id}/books")
  @ApiResponse(responseCode = "200", description = "Returns books in category")
  @ApiResponse(responseCode = "404", description = "Category not found")
  public ResponseEntity<PagedResponse<EntityModel<BookDTO>>> getAllBooksInCategory(
          @PathVariable Long id,
          @Parameter(hidden = true) Pageable pageable) {
    Page<BookDTO> books = categoryService.getBooksByCategory(id, pageable);
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
}
