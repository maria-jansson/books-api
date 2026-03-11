package booksapi.auth;

import booksapi.dto.AuthRequestDTO;
import booksapi.dto.AuthResponseDTO;
import booksapi.exception.InvalidCredentialsException;
import booksapi.exception.ResourceNotFoundException;
import booksapi.exception.UsernameAlreadyExistsException;
import booksapi.model.User;
import booksapi.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepo;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepo, JwtService jwtService, PasswordEncoder pwdEncoder) {
    this.userRepo = userRepo;
    this.jwtService = jwtService;
    this.passwordEncoder = pwdEncoder;
  }

  public AuthResponseDTO register(AuthRequestDTO data) {
    if (userRepo.findByUsername(data.username()).isPresent()) {
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
