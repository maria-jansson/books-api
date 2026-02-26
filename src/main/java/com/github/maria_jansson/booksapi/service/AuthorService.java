package com.github.maria_jansson.booksapi.service;

import com.github.maria_jansson.booksapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepo;

    public AuthorService (AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }
}
