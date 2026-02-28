package com.example.BoloDeLaMadre.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BoloDeLaMadre.entities.Usuario;
import com.example.BoloDeLaMadre.repositories.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Optional<Usuario> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public boolean exists(String username) {
        return repo.findByUsername(username).isPresent();
    }

    public Usuario createIfNotExists(String username, String rawPassword, String role) {
        return repo.findByUsername(username).orElseGet(() -> {
            Usuario u = Usuario.builder()
                .username(username)
                .password(encoder.encode(rawPassword))
                .role(role)
                .build();
            return repo.save(u);
        });
    }
}
