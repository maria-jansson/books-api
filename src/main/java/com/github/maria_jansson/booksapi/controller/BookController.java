package com.github.maria_jansson.booksapi.controller;

import com.github.maria_jansson.booksapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookRequestDTO data) {

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getOneBook(@PathVariable Long id) {

    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {

    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookRequestDTO data) {

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

    }
}
