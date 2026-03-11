package booksapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing an author of one or more books.
 */
@Getter
@Setter
@Entity
public class Author {
  @Id
  @GeneratedValue
  private Long id;
  @Column(columnDefinition = "TEXT")
  private String name;
  @ManyToMany(mappedBy = "authors")
  private List<Book> books;
}
