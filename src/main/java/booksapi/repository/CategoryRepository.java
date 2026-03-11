package booksapi.repository;

import booksapi.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Category entities.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
  /**
   * Finds categories whose name contains the given string, case-insensitive.
   *
   * @param name the string to search for
   * @param pageable pagination parameters
   * @return a page of matching categories
   */
  Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
