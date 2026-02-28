package com.example.BoloDeLaMadre.config;

import com.example.BoloDeLaMadre.entities.Usuario;
import com.example.BoloDeLaMadre.repositories.UsuarioRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioInitializer {

    @Bean
    ApplicationRunner init(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(encoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build();
                repo.save(admin);
            }
        };
    }
}