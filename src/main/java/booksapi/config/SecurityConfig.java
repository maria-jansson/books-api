package booksapi.config;

import booksapi.auth.JwtAuthenticationFilter;
import booksapi.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

/**
 * Security configuration for the application.
 * Configures JWT authentication, endpoint access rules, and password encoding.
 */
@Configuration
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;

  /**
   * Constructs a SecurityConfig with the required dependencies.
   *
   * @param jwtAuthFilter the filter for JWT token validation
   * @param userDetailsService the service for loading user details
   * @param objectMapper the mapper for serializing error responses
   */
  public SecurityConfig(
          JwtAuthenticationFilter jwtAuthFilter,
          UserDetailsService userDetailsService,
          ObjectMapper objectMapper) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.userDetailsService = userDetailsService;
    this.objectMapper = objectMapper;
  }

  /**
   * Configures the password encoder using BCrypt hashing.
   *
   * @return a BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the security filter chain with endpoint access rules,
   * stateless session management, and JWT authentication.
   *
   * @param http the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((
                            request,
                            response,
                            authException) -> {
                      ErrorResponse error = new ErrorResponse(
                              401,
                              "Authentication required",
                              LocalDateTime.now());
                      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                      response.setContentType("application/json");
                      objectMapper.writeValue(response.getWriter(), error);
                    })
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Configures the authentication provider with user details and password encoding.
   *
   * @return a DaoAuthenticationProvider instance
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  /**
   * Exposes the AuthenticationManager as a Spring bean.
   *
   * @param config the authentication configuration
   * @return the AuthenticationManager
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }
}
