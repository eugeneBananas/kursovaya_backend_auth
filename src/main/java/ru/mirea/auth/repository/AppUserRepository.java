package ru.mirea.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.auth.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findAll();
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByFullName(String fullName);
}
