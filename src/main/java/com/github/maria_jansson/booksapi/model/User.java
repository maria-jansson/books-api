package com.github.maria_jansson.booksapi.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
}
