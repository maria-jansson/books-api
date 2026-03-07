package com.github.maria_jansson.booksapi.repository;

import com.github.maria_jansson.booksapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
