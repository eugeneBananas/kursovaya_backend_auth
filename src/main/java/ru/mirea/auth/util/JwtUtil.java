package ru.mirea.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.mirea.auth.model.AppUser;

import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class); // Инициализация логгера

    private final String SECRET_KEY = "1234567891234567891234567891234512345678912345678912345678912345";

    public String generateToken(AppUser user) {
        logger.info("Generating token for user: {}", user.getEmail());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 500)) // 10 часов
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            logger.info("Extracting username from token.");
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }

    public boolean validateToken(String token) {
        try {
            logger.info("Validating token.");
            final String username = extractUsername(token);  // Извлекаем имя пользователя из токена (при необходимости)
            boolean isValid = !isTokenExpired(token);  // Проверяем, не истек ли срок действия токена
            logger.info("Token validation result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            boolean expired = expiration.before(new Date());
            logger.info("Token expiration check: {}", expired);
            return expired;
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return true; // Если произошла ошибка, считаем, что токен истек
        }
    }

}
