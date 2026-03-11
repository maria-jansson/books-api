package booksapi.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

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
