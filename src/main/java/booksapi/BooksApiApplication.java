package booksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Books API application.
 */
@SpringBootApplication
public class BooksApiApplication {
  /**
   * Starts the Spring Boot application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(BooksApiApplication.class, args);
  }
}
