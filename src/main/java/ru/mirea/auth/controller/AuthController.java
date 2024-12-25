package ru.mirea.auth.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mirea.auth.dto.AuthResponse;
import ru.mirea.auth.dto.LoginRequest;
import ru.mirea.auth.dto.RegisterRequest;
import ru.mirea.auth.dto.UserDTO;
import ru.mirea.auth.model.AppUser;
import ru.mirea.auth.repository.AppUserRepository;
import ru.mirea.auth.service.AuthenticationService;
import ru.mirea.auth.service.UserDetailsService;
import ru.mirea.auth.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Выполнить аутентификацию
        AppUser user = authenticationService.authenticateAndGetUser(request.getEmail(), request.getPassword());
        String token = authenticationService.generateToken(user);

        // Создать AuthResponse с необходимыми параметрами
        AuthResponse response = new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole().toString(),
                user.getFullName()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<UserDTO> getUserDetails(@RequestParam String username) {
        AppUser user = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/validateUser")
    public ResponseEntity<String> validateUser(@RequestParam String email) {
        try {
            userDetailsService.loadUserByUsername(email); // Пытаемся найти пользователя
            return ResponseEntity.ok("User exists");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        if (jwtUtil.validateToken(token)) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }
    }



    @PostMapping("/validateToken/extractUsername")
    public ResponseEntity<String> extractUsername(@RequestBody String token) {
        String username = jwtUtil.extractUsername(token);
        if (username != null) {
            return ResponseEntity.ok(username);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody RegisterRequest request) {
        AppUser user = authenticationService.register(request.getEmail(), request.getPassword(), request.getRole(), request.getFullName());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
