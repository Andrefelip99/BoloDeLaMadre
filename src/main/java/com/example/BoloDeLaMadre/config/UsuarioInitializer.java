package com.example.BoloDeLaMadre.config;

import com.example.BoloDeLaMadre.entities.Usuario;
import com.example.BoloDeLaMadre.repositories.UsuarioRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioInitializer {

    @Value("${app.bootstrap-admin.username:}")
    private String adminUsername;

    @Value("${app.bootstrap-admin.password:}")
    private String adminPassword;

    @Bean
    ApplicationRunner init(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                if (adminUsername.isBlank() || adminPassword.isBlank()) {
                    throw new IllegalStateException(
                        "Configure ADMIN_USERNAME e ADMIN_PASSWORD em arquivo.env para criar o administrador inicial."
                    );
                }

                Usuario admin = Usuario.builder()
                    .username(adminUsername)
                    .password(encoder.encode(adminPassword))
                    .role("ROLE_ADMIN")
                    .build();
                repo.save(admin);
            }
        };
    }
}
