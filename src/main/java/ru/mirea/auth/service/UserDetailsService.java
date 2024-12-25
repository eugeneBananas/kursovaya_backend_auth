package ru.mirea.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mirea.auth.model.AppUser;
import ru.mirea.auth.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AppUserRepository appUserRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    public UserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Trying to load user by username: {}", username);

        // Поиск по email
        AppUser appUser = appUserRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", username);  // этот лог должен сработать
                    throw new UsernameNotFoundException("User not found");
                });

        logger.info("Found user: {}", appUser.getEmail());

        // Логируем детали пользователя, которые передаем в User.builder()
        return User.builder()
                .username(appUser.getEmail()) // username = email
                .password(appUser.getPassword()) // Пароль должен быть зашифрован
                .roles(appUser.getRole())  // Роли
                .build();
    }

}
