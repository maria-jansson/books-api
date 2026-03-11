package booksapi.repository;

import booksapi.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Author entities.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
  /**
   * Finds authors whose name contains the given string, case-insensitive.
   *
   * @param name the string to search for
   * @param pageable pagination parameters
   * @return a page of matching authors
   */
  Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
