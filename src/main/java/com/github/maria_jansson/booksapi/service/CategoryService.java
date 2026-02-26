package com.github.maria_jansson.booksapi.service;

import com.github.maria_jansson.booksapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService (CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }
}
