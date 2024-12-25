package ru.mirea.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.auth.model.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    // Методы для поиска токенов можно добавить по необходимости
}
