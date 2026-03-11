package booksapi.auth;

import booksapi.model.User;
import booksapi.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of Spring Security's UserDetailsService.
 * Loads user-specific data from the database for authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepo;

  /**
   * Constructs a UserDetailsServiceImpl with the given UserRepository.
   *
   * @param userRepo the repository for user data access
   */
  public UserDetailsServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  /**
   * Loads a user by username for Spring Security authentication.
   *
   * @param username the username to look up
   * @return the UserDetails for the found user
   * @throws UsernameNotFoundException if no user with the given username exists
   */
  @Override
  @NonNull
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    User user =
        userRepo
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user with that username found."));
    return convertUserToUserDetails(user);
  }

  /**
   * Converts a User entity to a Spring Security UserDetails object.
   *
   * @param user the user entity to convert
   * @return a UserDetails object with the user's credentials and authorities
   */
  private UserDetails convertUserToUserDetails(User user) {
    return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities("ROLE_USER")
        .build();
  }
}
