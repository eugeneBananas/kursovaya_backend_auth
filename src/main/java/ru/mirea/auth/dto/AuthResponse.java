package ru.mirea.auth.dto;

public class AuthResponse {

    private String token;           // JWT-токен
    private String tokenType = "Bearer"; // Тип токена
    private Long userId;            // ID пользователя
    private String email;           // Email пользователя
    private String role;            // Роль пользователя
    private String fullName;

    // Конструктор
    public AuthResponse(String token, Long userId, String email, String role, String fullName) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    // Геттеры и сеттеры
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
