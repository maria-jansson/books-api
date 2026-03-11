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

/**
 * Service handling user authentication operations.
 * Manages user registration, login, and JWT token generation.
 */
@Service
public class AuthService {
  private final UserRepository userRepo;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructs an AuthService with the required dependencies.
   *
   * @param userRepo the repository for user data access
   * @param jwtService the service for JWT token generation
   * @param pwdEncoder the encoder for password hashing
   */
  public AuthService(UserRepository userRepo, JwtService jwtService, PasswordEncoder pwdEncoder) {
    this.userRepo = userRepo;
    this.jwtService = jwtService;
    this.passwordEncoder = pwdEncoder;
  }

  /**
   * Registers a new user account and returns a JWT token.
   *
   * @param data the registration request containing username and password
   * @return a JWT token for the newly created user
   * @throws UsernameAlreadyExistsException if the username is already taken
   */
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

  /**
   * Authenticates a user and returns a JWT token.
   *
   * @param data the login request containing username and password
   * @return a JWT token for the authenticated user
   * @throws ResourceNotFoundException if the username does not exist
   * @throws InvalidCredentialsException if the password is incorrect
   */
  public AuthResponseDTO login(AuthRequestDTO data) {
    User user =
        userRepo
            .findByUsername(data.username())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "User with name " + data.username() + " not found."));

    if (passwordEncoder.matches(data.password(), user.getPassword())) {
      String token = jwtService.generateToken(user.getUsername());

      return new AuthResponseDTO(token);
    } else {
      throw new InvalidCredentialsException("Could not log in.");
    }
  }
}
