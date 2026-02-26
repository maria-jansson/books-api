package com.github.maria_jansson.booksapi.model;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @ManyToOne
    private Author author;
    private String description;
    @ManyToOne
    private Category category;
    private String publisher;
    private double price;
    private String publishMonth;
    private int publishYear;

}
