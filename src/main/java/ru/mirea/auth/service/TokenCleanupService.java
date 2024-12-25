package ru.mirea.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {

    private final JwtTokenService jwtTokenService;

    @Autowired
    public TokenCleanupService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    // Планируем задачу, которая будет запускаться каждую ночь
    @Scheduled(cron = "0 0 0 * * ?")  // Каждую ночь в полночь
    public void cleanExpiredTokens() {
        jwtTokenService.deleteExpiredTokens();
    }
}
