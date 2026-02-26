package com.github.maria_jansson.booksapi.service;

import com.github.maria_jansson.booksapi.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepo;

    public BookService (BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }
}
