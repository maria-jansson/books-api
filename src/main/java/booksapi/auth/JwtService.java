package booksapi.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for JWT token generation and validation.
 * Handles creation, parsing, and verification of JSON Web Tokens.
 */
@Service
public class JwtService {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  /**
   * Generates a signed JWT token for the given username.
   *
   * @param username the username to include as the token subject
   * @return a signed JWT token string
   */
  public String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Extracts the username from a JWT token.
   *
   * @param token the JWT token string
   * @return the username stored as the token subject
   */
  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  /**
   * Validates a JWT token against a given username.
   *
   * @param token the JWT token string
   * @param username the expected username
   * @return true if the token is valid and not expired, false otherwise
   */
  public boolean isTokenValid(String token, String username) {
    String extractedUsername = extractUsername(token);
    return extractedUsername.equals(username) && !isTokenExpired(token);
  }

  /**
   * Checks whether a JWT token has expired.
   *
   * @param token the JWT token string
   * @return true if the token has expired, false otherwise
   */
  private boolean isTokenExpired(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration()
        .before(new Date());
  }

  /**
   * Returns the signing key used for JWT token generation and validation.
   *
   * @return the HMAC-SHA signing key derived from the configured secret
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
