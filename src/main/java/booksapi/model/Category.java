package booksapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a book category.
 */
@Getter
@Setter
@Entity
public class Category {
  @Id @GeneratedValue private Long id;
  private String name;

  @ManyToMany(mappedBy = "categories")
  private List<Book> books;
}
