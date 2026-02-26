package com.github.maria_jansson.booksapi.repository;

import com.github.maria_jansson.booksapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Book, Long> {
}
