package bj.orientation.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.web.servlet.HandlerInterceptor;

/** Protège l'espace de monitoring : exige l'en-tête X-Admin-Token égal au jeton configuré. */
public class AdminAuthInterceptor implements HandlerInterceptor {
  private final String token;

  public AdminAuthInterceptor(String token) {
    this.token = token;
  }

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    String fourni = request.getHeader("X-Admin-Token");
    if (token == null || token.isBlank() || fourni == null || !egalTempsConstant(token, fourni)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
    return true;
  }

  /** Comparaison en temps constant : ne révèle pas, par la durée, la longueur du préfixe juste. */
  private static boolean egalTempsConstant(String attendu, String fourni) {
    return MessageDigest.isEqual(
        attendu.getBytes(StandardCharsets.UTF_8), fourni.getBytes(StandardCharsets.UTF_8));
  }
}
