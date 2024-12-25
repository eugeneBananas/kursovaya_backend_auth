package ru.mirea.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.auth.model.AppUser;
import ru.mirea.auth.service.AppUserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserService.findAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        appUserService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable String email) {
        return appUserService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fullName/{fullName}")
    public ResponseEntity<AppUser> getUserByFullName(@PathVariable String fullName) {
        return appUserService.findByFullName(fullName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
