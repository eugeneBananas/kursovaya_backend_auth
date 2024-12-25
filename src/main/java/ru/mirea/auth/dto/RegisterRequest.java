package ru.mirea.auth.dto;

import ru.mirea.auth.model.AppUser;

public class RegisterRequest {
    public String email;
    public String password;
    public String role; // Используем AppUser.Role, если это вложенное перечисление
    public String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}