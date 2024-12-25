package ru.mirea.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.mirea.auth.filter.JwtAuthentication;

@Configuration
public class SecurityConfig {

    private final JwtAuthentication jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthentication jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Отключаем CSRF защиту (подходит для API)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Включаем CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Разрешаем OPTIONS запросы для всех
                        .requestMatchers("/auth/register", "/auth/login", "/auth/validateToken", "/auth/getUserDetails").permitAll() // Разрешаем доступ к регистрации, логину и проверке токена без авторизации
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Добавляем JWT фильтр

        return http.build(); // Метод для создания конфигурации
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Разрешаем доступ с фронтенда и с другого сервера (http://localhost:8082)
        configuration.addAllowedOrigin("http://localhost:5173"); // Разрешаем доступ с фронтенда
        configuration.addAllowedOrigin("http://localhost:8082"); // Разрешаем доступ с другого сервера

        configuration.addAllowedMethod("*"); // Разрешаем все HTTP методы (GET, POST, PUT, DELETE и т.д.)
        configuration.addAllowedHeader("*"); // Разрешаем все заголовки
        configuration.setAllowCredentials(true); // Разрешаем отправку куки и авторизационных заголовков

        // Разрешаем заголовок для авторизации
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Применяем конфигурацию ко всем путям
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используется Bcrypt для шифрования
    }
}


