package ru.mirea.auth.service;

import org.springframework.stereotype.Service;
import ru.mirea.auth.model.AppUser;
import ru.mirea.auth.repository.AppUserRepository;
import ru.mirea.auth.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerUser(RegisterRequest request) {
        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole().toUpperCase())
                .build();

        return appUserRepository.save(user);
    }

    public void saveUser(AppUser user) {
        // Шифрование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        appUserRepository.save(user);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public Optional<AppUser> findByFullName(String fullName) {
        return appUserRepository.findByFullName(fullName);
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }
}
