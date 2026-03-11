package booksapi.service;

import booksapi.dto.BookDTO;
import booksapi.dto.CategoryDTO;
import booksapi.exception.ResourceNotFoundException;
import booksapi.model.Category;
import booksapi.repository.CategoryRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for managing category resources.
 */
@Service
public class CategoryService {
  private final CategoryRepository categoryRepo;
  private final BookService bookService;

  /**
   * Constructs a CategoryService with the required dependencies.
   *
   * @param categoryRepo the repository for category data access
   * @param bookService the service for book data access
   */
  public CategoryService(CategoryRepository categoryRepo, BookService bookService) {
    this.categoryRepo = categoryRepo;
    this.bookService = bookService;
  }

  /**
   * Returns a paginated list of categories, optionally filtered by name.
   *
   * @param categoryName optional filter for category name
   * @param pageable pagination parameters
   * @return a page of category DTOs
   */
  public Page<CategoryDTO> getAllCategories(Optional<String> categoryName, Pageable pageable) {
    Page<Category> categories;

    if (categoryName.isPresent()) {
      categories = categoryRepo.findByNameContainingIgnoreCase(categoryName.get(), pageable);
    } else {
      categories = categoryRepo.findAll(pageable);
    }
    return categories.map(this::categoryToDTO);
  }

  /**
   * Returns a single category by ID.
   *
   * @param id the ID of the category to retrieve
   * @return the category DTO
   * @throws ResourceNotFoundException if no category with the given ID exists
   */
  public CategoryDTO getOneCategory(Long id) {
    Category category =
        categoryRepo
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Category with id " + id + " not found."));
    return new CategoryDTO(category.getId(), category.getName());
  }

  /**
   * Returns a paginated list of books in the specified category.
   *
   * @param id the ID of the category
   * @param pageable pagination parameters
   * @return a page of book DTOs
   */
  public Page<BookDTO> getBooksByCategory(Long id, Pageable pageable) {
    return bookService.getBooksByCategory(id, pageable);
  }

  /**
   * Converts a Category entity to a CategoryDTO.
   *
   * @param category the category entity to convert
   * @return the corresponding CategoryDTO
   */
  private CategoryDTO categoryToDTO(Category category) {
    return new CategoryDTO(category.getId(), category.getName());
  }
}
