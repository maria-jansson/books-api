package booksapi.repository;

import booksapi.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds a user by their username.
   *
   * @param username the username to search for
   * @return an Optional containing the user if found, or empty if not
   */
  Optional<User> findByUsername(String username);
}
