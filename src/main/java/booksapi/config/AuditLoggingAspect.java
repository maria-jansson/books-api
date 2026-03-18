package booksapi.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Aspect that logs all write operations for audit purposes.
 * Intercepts POST, PUT and DELETE requests and logs the authenticated user,
 * method name and controller class.
 */
@Aspect
@Component
public class AuditLoggingAspect {
  private static final Logger log = LoggerFactory.getLogger("AUDIT");

  /**
   * Logs a write operation after it has been successfully executed.
   * Captures the authenticated user from the security context.
   *
   * @param joinPoint - the join point providing method and class information
   */
  @AfterReturning(
      pointcut =
          "@annotation(org.springframework.web.bind.annotation.PostMapping)"
              + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
              + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  public void logWriteOperation(JoinPoint joinPoint) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String user = (auth != null) ? auth.getName() : "anonymous";
    log.info("user={} method={} class={}",
            user,
            joinPoint.getSignature().getName(),
            joinPoint.getTarget().getClass().getSimpleName());
  }
}
