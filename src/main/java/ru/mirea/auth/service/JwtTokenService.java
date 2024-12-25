package ru.mirea.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.auth.model.JwtToken;
import ru.mirea.auth.repository.JwtTokenRepository;
import ru.mirea.auth.util.JwtUtil;

import java.util.List;

@Service
public class JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtTokenService(JwtTokenRepository jwtTokenRepository, JwtUtil jwtUtil) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public void deleteExpiredTokens() {
        List<JwtToken> tokens = jwtTokenRepository.findAll();

        for (JwtToken token : tokens) {
            if (jwtUtil.isTokenExpired(token.getToken())) {
                // если jwtтокен истек, удаляем его
                jwtTokenRepository.delete(token);
            }
        }
    }
}
