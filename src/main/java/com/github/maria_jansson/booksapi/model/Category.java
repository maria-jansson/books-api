package com.github.maria_jansson.booksapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
