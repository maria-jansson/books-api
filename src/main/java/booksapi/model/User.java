package booksapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a registered user.
 */
@Table(name = "users")
@Getter
@Setter
@Entity
public class User {
  @Id @GeneratedValue private Long id;
  private String username;
  private String password;
}
