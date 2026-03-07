package com.github.maria_jansson.booksapi.auth;

import com.github.maria_jansson.booksapi.dto.AuthRequestDTO;
import com.github.maria_jansson.booksapi.dto.AuthResponseDTO;
import com.github.maria_jansson.booksapi.exception.InvalidCredentialsException;
import com.github.maria_jansson.booksapi.exception.ResourceNotFoundException;
import com.github.maria_jansson.booksapi.exception.UsernameAlreadyExistsException;
import com.github.maria_jansson.booksapi.model.User;
import com.github.maria_jansson.booksapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO register(AuthRequestDTO data) {
        if(userRepo.findByUsername(data.username()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already taken.");
        }

        String passwordHash = passwordEncoder.encode(data.password());
        User user = new User();
        user.setUsername(data.username());
        user.setPassword(passwordHash);
        userRepo.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(AuthRequestDTO data) {
        User user = userRepo.findByUsername(data.username()).orElseThrow(() ->
                new ResourceNotFoundException("User with name " + data.username() + " not found."));

        if (passwordEncoder.matches(data.password(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());

            return new AuthResponseDTO(token);
        } else {
            throw new InvalidCredentialsException("Could not log in.");
        }
    }
}
