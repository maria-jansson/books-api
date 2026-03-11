package booksapi.service;

import booksapi.dto.AuthorDTO;
import booksapi.dto.BookDTO;
import booksapi.exception.ResourceNotFoundException;
import booksapi.model.Author;
import booksapi.repository.AuthorRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for managing author resources.
 */
@Service
public class AuthorService {
  private final AuthorRepository authorRepo;
  private final BookService bookService;

  /**
   * Constructs an AuthorService with the required dependencies.
   *
   * @param authorRepo the repository for author data access
   * @param bookService the service for book data access
   */
  public AuthorService(AuthorRepository authorRepo, BookService bookService) {
    this.authorRepo = authorRepo;
    this.bookService = bookService;
  }

  /**
   * Returns a paginated list of authors, optionally filtered by name.
   *
   * @param authorName optional filter for author name
   * @param pageable pagination parameters
   * @return a page of author DTOs
   */
  public Page<AuthorDTO> getAllAuthors(Optional<String> authorName, Pageable pageable) {
    Page<Author> authors;
    if (authorName.isPresent()) {
      authors = authorRepo.findByNameContainingIgnoreCase(authorName.get(), pageable);
    } else {
      authors = authorRepo.findAll(pageable);
    }
    return authors.map(this::authorToDTO);
  }

  /**
   * Returns a single author by ID.
   *
   * @param id the ID of the author to retrieve
   * @return the author DTO
   * @throws ResourceNotFoundException if no author with the given ID exists
   */
  public AuthorDTO getOneAuthor(Long id) {
    Author author = authorRepo.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Author with id " + id + " not found."));
    return new AuthorDTO(author.getId(), author.getName());
  }

  /**
   * Returns a paginated list of books written by the specified author.
   *
   * @param id the ID of the author
   * @param pageable pagination parameters
   * @return a page of book DTOs
   */
  public Page<BookDTO> getBooksByAuthor(Long id, Pageable pageable) {
    return bookService.getBooksByAuthor(id, pageable);
  }

  /**
   * Converts an Author entity to an AuthorDTO.
   *
   * @param author the author entity to convert
   * @return the corresponding AuthorDTO
   */
  private AuthorDTO authorToDTO(Author author) {
    return new AuthorDTO(
            author.getId(),
            author.getName()
    );
  }
}