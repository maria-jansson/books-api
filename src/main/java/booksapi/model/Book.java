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
 * Entity representing a book in the database.
 */
@Getter
@Setter
@Entity
public class Book {
  @Id
  @GeneratedValue
  private Long id;
  @Column(columnDefinition = "TEXT")
  private String title;
  @ManyToMany
  private List<Author> authors;
  @Column(columnDefinition = "TEXT")
  private String description;
  @ManyToMany
  private List<Category> categories;
  private String publisher;
  private double price;
  private String publishMonth;
  private int publishYear;
}
