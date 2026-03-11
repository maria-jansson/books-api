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
