package booksapi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  @ManyToMany(mappedBy = "categories")
  private List<Book> books;
}
