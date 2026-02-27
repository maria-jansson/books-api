package com.github.maria_jansson.booksapi.controller;

import com.github.maria_jansson.booksapi.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getOneAuthor(@PathVariable Long id) {

    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {

    }
}
