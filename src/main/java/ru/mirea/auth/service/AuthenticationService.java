package ru.mirea.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mirea.auth.model.AppUser;
import ru.mirea.auth.model.Role;
import ru.mirea.auth.repository.AppUserRepository;
import ru.mirea.auth.util.JwtUtil;

@Service
public class AuthenticationService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }

    public AppUser authenticateAndGetUser(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }

    public String generateToken(AppUser user) {
        return jwtUtil.generateToken(user); // Уже реализованный метод в JwtUtil
    }

    public AppUser register(String email, String password, String role, String fullName) {
        if (userRepository.findByFullName(email).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role); // Используем AppUser.Role вместо внешнего Role
        user.setFullName(fullName);

        return userRepository.save(user);
    }

}

