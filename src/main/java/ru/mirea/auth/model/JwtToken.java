package ru.mirea.auth.model;

import jakarta.persistence.*;

@Entity
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Long userId;  // Или другой идентификатор пользователя, если нужно

    // Конструкторы, геттеры и сеттеры
    public JwtToken() {}

    public JwtToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
