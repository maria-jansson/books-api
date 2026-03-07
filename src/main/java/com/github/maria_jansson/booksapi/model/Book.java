package com.github.maria_jansson.booksapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
