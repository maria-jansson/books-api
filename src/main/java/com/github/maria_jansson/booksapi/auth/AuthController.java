package com.github.maria_jansson.booksapi.auth;

import com.github.maria_jansson.booksapi.dto.AuthRequestDTO;
import com.github.maria_jansson.booksapi.dto.AuthResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthRequestDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(data));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO data) {
        return ResponseEntity.ok(authService.login(data));
    }
}
