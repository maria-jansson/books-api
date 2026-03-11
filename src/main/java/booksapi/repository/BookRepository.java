package booksapi.repository;

import booksapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Book entities.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
  /**
   * Finds books by author ID.
   *
   * @param id the ID of the author
   * @param pageable pagination parameters
   * @return a page of books by the specified author
   */
  Page<Book> findByAuthorsId(Long id, Pageable pageable);

  /**
   * Finds books by category ID.
   *
   * @param id the ID of the category
   * @param pageable pagination parameters
   * @return a page of books in the specified category
   */
  Page<Book> findByCategoriesId(Long id, Pageable pageable);

  /**
   * Finds books whose author name contains the given string, case-insensitive.
   *
   * @param name the string to search for
   * @param pageable pagination parameters
   * @return a page of matching books
   */
  Page<Book> findByAuthorsNameContainingIgnoreCase(String name, Pageable pageable);

  /**
   * Finds books whose category name contains the given string, case-insensitive.
   *
   * @param name the string to search for
   * @param pageable pagination parameters
   * @return a page of matching books
   */
  Page<Book> findByCategoriesNameContainingIgnoreCase(String name, Pageable pageable);
}
